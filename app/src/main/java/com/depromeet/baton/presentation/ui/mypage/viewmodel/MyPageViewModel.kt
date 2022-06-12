package com.depromeet.baton.presentation.ui.mypage.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.data.response.UserProfileResponse
import com.depromeet.baton.domain.model.UserInfo
import com.depromeet.baton.domain.repository.UserinfoRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.util.uriConverter
import com.depromeet.bds.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val userinfoRepository: UserinfoRepository
): BaseViewModel() {

    private val context : Context = application

    private val _uiState: MutableStateFlow<MypageUiState> = MutableStateFlow(MypageUiState(profileImage = null))
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private val _res = MutableLiveData<UserInfo>()
    val res get()=_res

    init {
        //APi 호출
        viewModelScope.launch {
            runCatching {
                val res = userinfoRepository.getUserProfile(1)
                when(res){
                    is NetworkResult.Success<UserProfileResponse> ->{
                        _uiState.update {
                            MypageUiState(
                                nickName = res.data!!.name,
                                phoneNumber = res.data!!.phone_number.replace(Regex("[^0-9]*"),""),
                                joinDate = res.data!!.created_on ,
                                profileImage = uriConverter(context, R.drawable.ic_img_profile_startled_56)
                            )
                        }
                    }

                }
            }.onFailure {
                Timber.e("fail "+ it.message)
            }
        }
    }


    fun updateNickname(nickName: String ){
        _uiState.update { it.copy(nickName = nickName) }
    }

    fun updateProfileImg(profileImage: Uri){
        _uiState.update { it.copy(profileImage= profileImage) }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }


    sealed interface ViewEvent {

    }

}
data class MypageUiState(
    val nickName: String?="",
    val phoneNumber: String?="",
    val joinDate: String?="",
    val profileImage: Uri?,
){
    val isLoading = nickName==""
}