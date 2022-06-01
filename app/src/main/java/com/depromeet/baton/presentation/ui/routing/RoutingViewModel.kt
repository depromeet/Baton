package com.depromeet.baton.presentation.ui.routing

import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RoutingViewModel @Inject constructor() : BaseViewModel() {

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    //BEAN:
    // 로그인 되어있지 않다면, 로그인 화면으로 랜딩해야한다.

    init {

        addViewEvent(ViewEvent.ToLogIn)
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
