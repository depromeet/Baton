package com.depromeet.baton.presentation.ui.detail.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.R
import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.BookmarkRepository
import com.depromeet.baton.domain.repository.SearchRepository
import com.depromeet.baton.domain.repository.TicketInfoRepository
import com.depromeet.baton.domain.usecase.GetFilteredTicketUseCase
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.util.BatonSpfManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

import javax.inject.Inject

@HiltViewModel
class TicketMoreViewModel @Inject constructor(
    private val spfManager: BatonSpfManager,
    private val savedStateHandle: SavedStateHandle,
    private val getFilteredTicketUseCase: GetFilteredTicketUseCase,
    private val ticketInfoRepository: TicketInfoRepository,
    private val authRepository: AuthRepository,
    private val bookmarkRepository: BookmarkRepository,
) : BaseViewModel() {
    private val _uiState = MutableStateFlow<List<FilteredTicket>>(emptyList())
    val uiState = _uiState.asStateFlow()

    private val _networkState = MutableStateFlow<TicketMoreNetwork>(TicketMoreNetwork.Loading)
    val networkState = _networkState.asStateFlow()

    private val MAX_ITEM = 5

    init {
        initState()
    }

    fun initState() {
        viewModelScope.launch {
            runCatching {
                //TODO 추천 아이템 불러오기
                getFilteredTicketUseCase.execute(
                    page = 0,
                    size = MAX_ITEM,
                    longitude = spfManager.getMyLongitude(),
                    latitude = spfManager.getMyLatitude(),
                    maxDistance = spfManager.getMaxDistance().getDistance(),
                    sortType = "DISTANCE"
                )
            }.onSuccess {
                when (it) {
                    is UIState.Success<*> -> {
                        @Suppress("UNCHECKED_CAST")
                        val list = it.data as List<FilteredTicket>
                        if (list.isNotEmpty()) {
                            _uiState.update { list }
                        }
                    }
                }
            }.onFailure { error ->
                Timber.e(error.message)
                _networkState.update { TicketMoreNetwork.Failure(error.message.toString()) }
            }
        }
    }

    fun postMoreTicketBookmark(ticketID: Int) {
        viewModelScope.launch {
            val userId = authRepository.authInfo!!.userId
            runCatching { bookmarkRepository.postBookmark(userId, ticketID) }
                .onSuccess { Timber.d("북마크 추가 성공") }
                .onFailure { Timber.e(it.message) }
        }
    }

    fun deleteMoreTicketBookmark(ticketID: Int) {
        viewModelScope.launch {
            runCatching { bookmarkRepository.deleteBookmark(ticketID) }
                .onSuccess { Timber.d("북마크 삭제 성공") }
                .onFailure { Timber.e(it.message) }
        }
    }
}

sealed class TicketMoreNetwork() {
    object Success : TicketMoreNetwork()
    data class Failure(val msg: String) : TicketMoreNetwork()
    object Loading : TicketMoreNetwork()
}