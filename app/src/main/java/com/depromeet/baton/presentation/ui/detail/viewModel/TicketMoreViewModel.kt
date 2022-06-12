package com.depromeet.baton.presentation.ui.detail.viewModel

import androidx.lifecycle.viewModelScope
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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TicketMoreViewModel @Inject constructor(
    private val ticketInfoRepository: TicketInfoRepository,
    private val spfManager: BatonSpfManager
):BaseViewModel(){
    private val _uiState = MutableStateFlow<List<ResponseFilteredTicket>>(emptyList())
    val uiState = _uiState.asStateFlow()

    private val _networkState = MutableStateFlow<TicketMoreNetwork>(TicketMoreNetwork.Loading)
    val networkState = _networkState.asStateFlow()

    init{
        viewModelScope.launch {
            runCatching {
                //TODO 추천 아이템 불러오기
                ticketInfoRepository.getMoreTicket(5, spfManager.getMyLongitude(),spfManager.getMyLatitude(),spfManager.getMaxDistance().getDistance())
            }.onSuccess {
                //Api result
                res -> if(res is NetworkResult.Success )  res?.let {
                   val list=  res.data?.content?.map{ ResponseFilteredTicket(it.id, it.location,it.address, it.price, it.mainImage?:"",
                       it.createAt, it.state,it.tags, it.images?: emptyList(),it.isMembership,it.remainingNumber?:0,
                       it.expiryDate?:"",it.latitude,it.longitude,it.distance)}
                    _uiState.update { list ?: emptyList() }
                    _networkState.update { TicketMoreNetwork.Success }
                }
            }.onFailure {
                error ->
                    Timber.e(error.message)
                    _networkState.update { TicketMoreNetwork.Failure(error.message.toString()) }
            }
        }
    }

}

sealed class TicketMoreNetwork(){
    object  Success : TicketMoreNetwork()
    data class Failure(val msg : String) : TicketMoreNetwork()
    object Loading : TicketMoreNetwork()
}