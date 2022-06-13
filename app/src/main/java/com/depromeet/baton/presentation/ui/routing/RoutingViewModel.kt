package com.depromeet.baton.presentation.ui.routing

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutingViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    init {

        // 로그인 되어있지 않다면, 로그인 화면으로 랜딩해야한다.
        viewModelScope.launch {
            if (authRepository.isLoggedIn()) {
                addViewEvent(ViewEvent.ToHome)
            } else {
                addViewEvent(ViewEvent.ToLogIn)
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
    }
}
