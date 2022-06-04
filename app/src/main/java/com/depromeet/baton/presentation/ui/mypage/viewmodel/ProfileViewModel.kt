package com.depromeet.baton.presentation.ui.mypage.viewmodel

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.depromeet.baton.presentation.ui.mypage.model.ProfileIcon
import com.depromeet.baton.presentation.util.RegexConstant
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    private val context : Context = application

    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private val _profileImageUri = MutableLiveData<Uri>()
    val profileImageUri : LiveData<Uri> get() = _profileImageUri


    private val _temporaryUiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(createState())
    val temporaryUiState = _temporaryUiState.asStateFlow()

    private val _isChangedImage = MutableLiveData<Boolean>(false)
    val isChangedImage : LiveData<Boolean> get()= _isChangedImage


    private fun createState(): ProfileUiState {
        return ProfileUiState(
            nickName = "김바통",
            phoneNumber = "01000000000",
            joinDate="2020.02.02",
            profileImage = uriConverter(com.depromeet.bds.R.drawable.ic_img_profile_smirk_56),
            onNickNameChanged = ::handleNickNameChanged,
            onPhoneNumberChanged = ::handlePhoneNumberChanged,
            onProfileChanged = ::submitProfileImg,
            isChanged = false,
            onSubmit = ::submitProfile
        )
    }


    private fun handleNickNameChanged(text: Editable?) {
        _temporaryUiState.update { it.copy(nickName = text.toString() , isChanged =it.nickName!=text.toString()) }
    }

    private fun handlePhoneNumberChanged(text: Editable?) {
        _temporaryUiState.update { it.copy(phoneNumber = text.toString() ,isChanged = it.phoneNumber!=text.toString()) }
    }

    fun onClickEmotion(image: ProfileIcon){
        _temporaryUiState.update { it.copy(profileImage = uriConverter(image.size56)) }
    }

    fun setImage(image: Image){
       _temporaryUiState.update { it.copy(profileImage = image.uri) }
    }

    fun submitProfileImg(){
        //Bottom 에서 확인 눌렀을 때
        _uiState.update { it.copy( profileImage = temporaryUiState.value.profileImage ) }
        _viewEvents.update { it +ViewEvent.ToSettingProfileImg}
    }

    private fun submitProfile(){
        _uiState.update { _temporaryUiState.value }
        _viewEvents.update { it +ViewEvent.ToBack}
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    private fun uriConverter(id: Int):Uri{
        val resourceId = id
        val uriParser= Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(context.resources.getResourcePackageName(resourceId))
            .appendPath(context.resources.getResourceTypeName(resourceId))
            .appendPath(context.resources.getResourceEntryName(resourceId))
            .build()

        return uriParser
    }

    sealed interface ViewEvent {
        object ToBack: ViewEvent
        object ToSettingProfileImg: ViewEvent
    }

}
 data class ProfileUiState(
     val nickName: String,
     val phoneNumber: String,
     val joinDate: String,
     val profileImage: Uri,
     val onNickNameChanged: (Editable?) -> Unit,
     val onPhoneNumberChanged: (Editable?) -> Unit,
     val onProfileChanged: () -> Unit,
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