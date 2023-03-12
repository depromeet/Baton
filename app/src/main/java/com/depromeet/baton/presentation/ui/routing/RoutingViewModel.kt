package com.depromeet.baton.presentation.ui.routing

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.api.user.TokenApi
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.UserinfoRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RoutingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userinfoRepository: UserinfoRepository,
) : BaseViewModel() {

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    init {
        // 로그인 되어있지 않다면, 로그인 화면으로 랜딩해야한다.
        viewModelScope.launch {
            if (authRepository.isLoggedIn()) {
                Timber.e("checking token")
                authValidation()
            } else {
                addViewEvent(ViewEvent.ToLogIn)
            }
        }
    }


    private fun authValidation(){
        viewModelScope.launch {
            userinfoRepository.authValidation(authRepository.authInfo?.accessToken!!,authRepository.authInfo?.refreshToken!!).let{
                Timber.e(it.toString())
                when(it){
                    is TokenApi.RefreshResult.Success ->{
                        authRepository.setAuthInfo(it.response.access_token!!,it.response.refresh_token!!)
                        addViewEvent(ViewEvent.ToHome)
                    }
                    else->{
                        authRepository.logout()
                        addViewEvent(ViewEvent.AuthToast)
                    }
                }
            }
        }
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    sealed interface ViewEvent {
        object ToHome : ViewEvent
        object ToLogIn : ViewEvent
        object AuthToast : ViewEvent
    }
}
