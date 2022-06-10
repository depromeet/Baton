package com.depromeet.baton.presentation.ui.mypage.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.text.Editable
import androidx.lifecycle.SavedStateHandle
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.util.RegexConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel@Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle
): BaseViewModel(){

    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ProfileViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()


    private fun createState(): ProfileUiState {

        return ProfileUiState(
            nickName = "",
            phoneNumber = "",
            profileImage =Uri.parse(""),
            onNickNameChanged = ::handleNickNameChanged,
            onPhoneNumberChanged = ::handlePhoneNumberChanged,
            onProfileChanged = ::submitProfileImg,
            isChanged = false,
            onSubmit = ::submitProfile
        )
    }

    fun initProfileInfo( name : String , phone :String, img : String){
        _uiState.update { it.copy( nickName = name , phoneNumber = phone , profileImage = Uri.parse(img)) }
    }


    private fun handleNickNameChanged(text: Editable?) {
        _uiState.update { it.copy(nickName = text.toString() , isChanged =it.nickName!=text.toString()) }
    }

    private fun handlePhoneNumberChanged(text: Editable?) {
        _uiState.update { it.copy(phoneNumber = text.toString() ,isChanged = it.phoneNumber!=text.toString()) }
    }


    fun submitProfileImg(uri : Uri){
        //Bottom 에서 확인 눌렀을 때
        _uiState.update { it.copy( profileImage = uri , isChanged = true) }
        addViewEvent(ProfileViewEvent.EventUpdateProfileImage)
    }

     fun submitProfile(){
        //변경 API 호출
        addViewEvent(ProfileViewEvent.EventUpdateProfileInfo)
    }

    fun consumeViewEvent(profileViewEvent: ProfileViewEvent) {
        _viewEvents.update { it - profileViewEvent }
    }

    private fun addViewEvent(profileViewEvent: ProfileViewEvent) {
        _viewEvents.update { it + profileViewEvent }
    }



    sealed interface ProfileViewEvent {
        object EventUpdateProfileInfo: ProfileViewEvent
        object EventUpdateProfileImage: ProfileViewEvent
        object EventToBack : ProfileViewEvent
    }


}
 data class ProfileUiState(
     val nickName: String,
     val phoneNumber: String,
     val profileImage: Uri,
     val onNickNameChanged: (Editable?) -> Unit,
     val onPhoneNumberChanged: (Editable?) -> Unit,
     val onProfileChanged: (Uri) -> Unit,
     val isChanged: Boolean,
     val onSubmit: () -> Unit,
) {
    val nickNameErrorReason =
        if (RegexConstant.ONLY_COMPLETE_HANGLE_SPACE.matches(nickName)) null
        else "올바른 닉네임을 입력해주세요."
    val phoneNumberErrorReason =
        if (RegexConstant.ONLY_NUMBERS.matches(phoneNumber) && phoneNumber.length == 11) null
        else "올바른 휴대폰 번호를 입력해주세요."

    val isEnabled = isChanged &&nickName.isNotBlank() &&
            nickNameErrorReason== null &&
            phoneNumber.isNotBlank() &&
            phoneNumberErrorReason == null
}