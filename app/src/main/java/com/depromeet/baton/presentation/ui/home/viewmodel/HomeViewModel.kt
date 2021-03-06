package com.depromeet.baton.presentation.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.BookmarkRepository
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookmarkRepository: BookmarkRepository,
) : BaseViewModel() {
    private val _viewEvents: MutableStateFlow<List<HomeViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private val _homeUiState: MutableStateFlow<HomeUiState> = MutableStateFlow(createHomeState())
    val homeUiState = _homeUiState.asStateFlow()

    private val _initLocation = MutableLiveData<String?>()

    private val _fromAddress = SingleLiveEvent<Any>()
    val fromAddress: SingleLiveEvent<Any> = _fromAddress

    fun checkToolTipState(ticketCount: Int, location: String) {
        if (ticketCount == 0) {
            _initLocation.value = location
            handleToolTipShow()
        }
    }

    fun setFromAddress(state: Boolean) {
        _fromAddress.value = state
    }

    private fun createHomeState(): HomeUiState {
        return HomeUiState(
            onSetCurrentLocationClick = ::handleSetCurrentLocationClick,
            onSearchClick = ::handleSearchClick,
            onAlarmClick = ::handleAlarmClick,
            onHowToClick = ::handleHowToClick,
            onQuickClick = ::handleQuickClick,
            onWritePostClick = ::handleWritePostClick,
            onToolTip = ::handleToolTipShow,
        )
    }

    private fun handleSetCurrentLocationClick() {
        addViewEvent(HomeViewEvent.ToLocation)
    }

    private fun handleSearchClick() {
        addViewEvent(HomeViewEvent.ToSearch)
    }

    private fun handleAlarmClick() {
        addViewEvent(HomeViewEvent.ToAlarm)
    }

    private fun handleHowToClick() {
        addViewEvent(HomeViewEvent.ToHowTo)
    }

    private fun handleQuickClick(type: Any) {
        when (type) {
            TicketKind.HEALTH -> addViewEvent(HomeViewEvent.ToQuickHealth)
            TicketKind.PT -> addViewEvent(HomeViewEvent.ToQuickPt)
            TicketKind.PILATES_YOGA -> addViewEvent(HomeViewEvent.ToQuickPilates)
            TicketKind.ETC -> addViewEvent(HomeViewEvent.ToQuickEtc)
        }
    }

    private fun handleWritePostClick() {
        addViewEvent(HomeViewEvent.ToWritePost)
    }

    private fun handleToolTipShow() {
        addViewEvent(HomeViewEvent.ShowToolTip)
    }

    private fun addViewEvent(viewEvent: HomeViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: HomeViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    fun postBookmark(ticketId: Int) {
        viewModelScope.launch {
            val userId = authRepository.authInfo!!.userId
            runCatching { bookmarkRepository.postBookmark(userId, ticketId) }
                .onSuccess { }
                .onFailure { Timber.e("????????? ?????? ??????") }
        }
    }

    fun deleteBookmark(ticketId: Int) {
        viewModelScope.launch {
            runCatching { bookmarkRepository.deleteBookmark(ticketId) }
                .onSuccess { }
                .onFailure { Timber.e("????????? ?????? ??????") }
        }
    }

    sealed interface HomeViewEvent {
        object ToLocation : HomeViewEvent
        object ToSearch : HomeViewEvent
        object ToAlarm : HomeViewEvent
        object ToHowTo : HomeViewEvent
        object ToQuickHealth : HomeViewEvent
        object ToQuickPt : HomeViewEvent
        object ToQuickPilates : HomeViewEvent
        object ToQuickEtc : HomeViewEvent
        object ToWritePost : HomeViewEvent
        object ShowToolTip : HomeViewEvent
    }

    data class HomeUiState(
        val onSetCurrentLocationClick: () -> Unit,
        val onSearchClick: () -> Unit,
        val onAlarmClick: () -> Unit,
        val onHowToClick: () -> Unit,
        val onQuickClick: (Any) -> Unit,
        val onWritePostClick: () -> Unit,
        val onToolTip: () -> Unit,
    )
}

