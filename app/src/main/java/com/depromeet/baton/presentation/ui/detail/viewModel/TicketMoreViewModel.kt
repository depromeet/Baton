package com.depromeet.baton.presentation.ui.detail.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.R
import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.domain.repository.TicketInfoRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.home.view.TicketItem
import com.depromeet.baton.util.BatonSpfManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class TicketMoreViewModel @Inject constructor():BaseViewModel(){
    private val _uiState = MutableStateFlow<List<ResponseFilteredTicket>>(emptyList())
    val uiState = _uiState.asStateFlow()

    private val _networkState = MutableStateFlow<TicketMoreNetwork>(TicketMoreNetwork.Loading)
    val networkState = _networkState.asStateFlow()

    private val MAX_ITEM = 5

    init{
        initState()
    }
    fun initState(){
        viewModelScope.launch {
            runCatching {
                //TODO 추천 아이템 불러오기
                ticketInfoRepository.getMoreTicket(MAX_ITEM , spfManager.getMyLongitude(),spfManager.getMyLatitude(),spfManager.getMaxDistance().getDistance())
            }.onSuccess {
                //Api result
                    res -> if(res is NetworkResult.Success )  res?.let {
                val ticketId = savedStateHandle.get<Int>("ticketId")!!
                val list = res.data?.content!!.filter { it.id!= ticketId}
                _uiState.update { list }
                _networkState.update { TicketMoreNetwork.Success }
            }
            }.onFailure {
                    error ->
                Timber.e(error.message)
                _networkState.update { TicketMoreNetwork.Failure(error.message.toString()) }
            }
        }
    }


    private fun initState():List<ResponseFilteredTicket>{
        //Api
        return  arrayListOf(
        )
    }
}

sealed class TicketMoreNetwork(){
    object  Success : TicketMoreNetwork()
    data class Failure(val msg : String) : TicketMoreNetwork()
    object Loading : TicketMoreNetwork()
}