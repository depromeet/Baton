package com.depromeet.baton.presentation.ui.mypage.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.UserinfoRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.remote.user.UserAccount
import com.depromeet.baton.util.BatonSpfManager
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
    private val spfManager: BatonSpfManager,
    private val authRepository: AuthRepository,
    private val savedStateHandle: SavedStateHandle,
    private val userinfoRepository: UserinfoRepository
) : BaseViewModel() {

    private val context: Context = application

    private val _uiState: MutableStateFlow<MypageUiState> =
        MutableStateFlow(MypageUiState(profileImage = null, account = null))
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()


    fun getProfile() {
        viewModelScope.launch {
            runCatching {
                val res = userinfoRepository.getUserProfile(authRepository.authInfo!!.userId)
                when (res) {
                    is NetworkResult.Success -> {
                        _uiState.update {
                            MypageUiState(
                                nickName = res.data!!.nickname,
                                phoneNumber = res.data!!.phone_number.replace(Regex("[^0-9]*"), ""),
                                joinDate = res.data!!.created_on,
                                profileImage = res.data!!.profileImg,
                                account = res.data!!.account
                            )
                        }
                    }
                    is NetworkResult.Error -> {
                        Timber.e(res.message)
                    }
                }
            }
        }
    }


    fun updateProfile(nickName: String, phoneNumber: String,profileImage: String?) {
        _uiState.update { it.copy(nickName = nickName, phoneNumber = phoneNumber,profileImage = profileImage) }
    }

    fun logout() {
        authRepository.logout()
    }

    fun deleteUser() {
       viewModelScope.launch {
            runCatching {
               val userId = authRepository.authInfo!!.userId
               userinfoRepository.deleteUser(userId)
            }.onSuccess {
                when (it) {
                    is NetworkResult.Success -> {
                        addViewEvent(ViewEvent.EventWithdrawal("탈퇴 되었습니다."))
                          authRepository.logout()
                          spfManager.clearAll()
                    }
                    is NetworkResult.Error -> {
                        Timber.e(it.message)
                        addViewEvent(ViewEvent.EventWithdrawal("탈퇴에 실패했습니다."))
                    }
                }
            }
                .onFailure { it -> Timber.e(it.message) }
        }

    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }


    sealed class ViewEvent {
       data class EventWithdrawal(val msg :String): ViewEvent()
    }

}

data class MypageUiState(
    val nickName: String? = "",
    val phoneNumber: String? = "",
    val joinDate: String? = "",
    val profileImage: String?,
    val account: UserAccount?,
) {
    val isLoading = nickName == ""
    val img = if(profileImage !=null) Uri.parse(profileImage) else null
}