package com.depromeet.baton.presentation.ui.mypage.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.UserinfoRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.util.MultiPartResolver
import com.depromeet.baton.presentation.util.RegexConstant
import com.depromeet.baton.presentation.util.uriConverter
import com.depromeet.baton.util.BatonSpfManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import java.net.URI
import java.net.URL
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel@Inject constructor(
    private val userinfoRepository: UserinfoRepository,
    private val authRepository: AuthRepository,
    private val spfManager: BatonSpfManager,
    application: Application
): AndroidViewModel(application){

    private val context = application.baseContext

    private val multiPartResolver = MultiPartResolver(context)

    val emptyIcon = uriConverter(context, com.depromeet.bds.R.drawable.ic_img_profile_basic_smile_96)

    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ProfileViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()


    private fun createState(): ProfileUiState {
        return ProfileUiState(
            nickName = "",
            phoneNumber = "",
            profileImage = null ,
            onNickNameChanged = ::handleNickNameChanged,
            onPhoneNumberChanged = ::handlePhoneNumberChanged,
            onProfileChanged = ::submitProfileImg,
            isChanged = false,
            isLoading = true,
            onSubmit = ::submitProfile
        )
    }

    fun initProfileInfo(){
        viewModelScope.launch {
            runCatching {
                val res = userinfoRepository.getUserProfile(authRepository.authInfo!!.userId)
                when (res) {
                    is NetworkResult.Success -> {
                        _uiState.update {
                            it.copy(
                                nickName = res.data!!.nickname,
                                phoneNumber = res.data!!.phone_number ,
                                profileImage = res.data!!.profileImg?.let { Uri.parse(it) }, isLoading = false) }
                    }
                    is NetworkResult.Error -> {
                        Timber.e(res.message)
                    }
                }
            }
        }
    }


    private fun handleNickNameChanged(text: Editable?) {
        _uiState.update { it.copy(nickName = text.toString() , isChanged =it.nickName!=text.toString()) }
    }

    private fun handlePhoneNumberChanged(text: Editable?) {;
        _uiState.update { it.copy(phoneNumber = text.toString() ,isChanged = it.phoneNumber!=text.toString()) }
    }


    fun submitProfileImg(uri : Uri?){
        //Bottom 에서 확인 눌렀을 때
        _uiState.update { it.copy( profileImage = uri , isChanged = true) }
        addViewEvent(ProfileViewEvent.EventUpdateProfileImage)
    }


     fun submitProfile(){
        //변경 API 호출
         if(uiState.value.isEnabled)
             viewModelScope.launch {
                 val userId = authRepository.authInfo!!.userId
                 runCatching {
                     userinfoRepository.updateUserProfile(userId,uiState.value.nickName, uiState.value.phoneNumber)
                 }.onSuccess {
                     when(it){
                         is NetworkResult.Success->{ updateProfileImage() }
                         is NetworkResult.Error->{Timber.e(it.message)}
                     }
                 }
             }
    }
    private fun updateProfileImage(){
        viewModelScope.launch {
            val userId = authRepository.authInfo!!.userId
            runCatching {
                with(uiState.value.profileImage){
                    if(this != null){
                        if(this.toString().contains("https")) /** icon 변경 **/
                            userinfoRepository.updateProfileIcon(authRepository.authInfo!!.accessToken,userId,this.toString())
                        else  multiPartResolver.createSingleImgMultiPart(this).let {
                            userinfoRepository.updateProfileImage(userId, it)
                        }
                    }
                    else userinfoRepository.deleteProfileImage(userId)
                }
            }.onSuccess {
                when(it){
                    is NetworkResult.Success->{     addViewEvent(ProfileViewEvent.EventUpdateProfileInfo) }
                    is NetworkResult.Error ->{Timber.e(it.message)}
                }
            }
        }
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
     val profileImage: Uri?,
     val onNickNameChanged: (Editable?) -> Unit,
     val onPhoneNumberChanged: (Editable?) -> Unit,
     val onProfileChanged: (Uri) -> Unit,
     val isChanged: Boolean,
     val isLoading : Boolean,
     val onSubmit: () -> Unit,
) {
    val nickNameErrorReason =
        if (nickName.isNotBlank()&&RegexConstant.NICKNAME_REGEX.matches(nickName)) null
        else  "올바른 닉네임을 입력해주세요."
    val phoneNumberErrorReason =
        if (RegexConstant.ONLY_NUMBERS.matches(phoneNumber) && phoneNumber.length == 11) null
        else "올바른 휴대폰 번호를 입력해주세요."

    val isEnabled = isChanged &&nickName.isNotBlank() &&
            nickNameErrorReason== null &&
            phoneNumber.isNotBlank() &&
            phoneNumberErrorReason == null
}