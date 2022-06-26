package com.depromeet.baton.presentation.ui.mypage.viewmodel

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.data.response.UserBuyListResponse
import com.depromeet.baton.domain.model.MypageTicketResponse
import com.depromeet.baton.domain.model.TicketInfo
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.domain.model.TicketStatus
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.TicketInfoRepository
import com.depromeet.baton.domain.repository.UserinfoRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.detail.viewModel.TicketDetailNetWork
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketItem
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketListItem
import com.depromeet.baton.presentation.util.dateFormatUtil
import com.depromeet.baton.util.BatonSpfManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SaleHistoryViewModel @Inject constructor(
    private val userinfoRepository: UserinfoRepository,
    private val ticketinfoRepository: TicketInfoRepository,
    private val authRepository: AuthRepository,
    val spfManager: BatonSpfManager
) : BaseViewModel() {
    private val _uiState: MutableStateFlow<SaleHistoryUiState> =
        MutableStateFlow(SaleHistoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _soldoutUiState: MutableStateFlow<SoldoutHistoryUiState> =
        MutableStateFlow(SoldoutHistoryUiState())
    val soldoutUiState = _soldoutUiState.asStateFlow()


    fun getSaleHistory() {
        viewModelScope.launch {
            //authRepository.authInfo!!.userId
            runCatching {
                _uiState.update { it.copy( isLoading = true) }
                userinfoRepository.getUserSellList(authRepository.authInfo!!.userId, TicketState.SALE.option)
            }.onSuccess { res ->
                run {
                    when (res) {
                        is NetworkResult.Success -> {
                            val list = res.data?.toListItems()
                            _uiState.update { it.copy(list = list, isLoading = false) }
                        }
                        is NetworkResult.Error -> {
                            Timber.e(res.message)
                        }
                    }
                }
            }.onFailure { e ->
                Timber.e(e.toString())
            }
        }
    }

    fun deleteTicket(ticketId: Int) {
        viewModelScope.launch {
           runCatching {
                ticketinfoRepository.deleteTicket(ticketId)
               getSaleHistory()
               getSoldoutHistory()
           }.onFailure {
                Timber.e(it.message)
           }
        }
    }

    fun getSoldoutHistory() {
        viewModelScope.launch {
            runCatching {
                _soldoutUiState.update { it.copy( isLoading = true) }
                userinfoRepository.getUserSellList(authRepository.authInfo!!.userId, TicketState.SOLDOUT.option)
            }.onSuccess { res ->
                run {
                    when (res) {
                        is NetworkResult.Success -> {
                            val list = res.data?.toListItems()
                            _soldoutUiState.update { it.copy(list = list, isLoading = false) }
                        }
                        is NetworkResult.Error -> {
                            Timber.e( res.message)
                        }
                    }
                }
            }.onFailure { e ->
                Timber.e(e.message)
            }
        }
    }


    //TODO 판매상태변경 => removeItem 처리할지 재로딩 할지 결정
    fun changeStatus(ticketId: Int, status: Int) {
        //변경후 초기화
        viewModelScope.launch {
            runCatching {
                ticketinfoRepository.updateTicketState(ticketId, TicketStatus.values()[status].name )
            }.onSuccess {
                when(it){
                    is NetworkResult.Success ->{
                        getSaleHistory()
                        getSoldoutHistory()
                    }
                    is NetworkResult.Error->{
                        Timber.e("${it.message}")
                    }
                }
            }.onFailure {
                Timber.e(it.message)
            }
        }

    }


    private fun List<MypageTicketResponse>.toListItems(): List<SaleTicketListItem> {
        val result = arrayListOf<SaleTicketListItem>() // 결과를 리턴할 리스트
        var groupHeaderDate = "" // 그룹날짜
        this.forEachIndexed { index, item ->
            // 날짜가 달라지면 그룹헤더를 추가.
            if (item.createAt != null) {
                if (groupHeaderDate != dateFormatUtil(item.createAt)) {
                    if (index != 0) result.add(
                        SaleTicketListItem.Footer(
                            SaleTicketItem(
                                item.id,
                                item.createAt,
                                item
                            )
                        )
                    )
                    result.add(
                        SaleTicketListItem.Header(
                            SaleTicketItem(
                                item.id,
                                item.createAt,
                                item
                            )
                        )
                    )
                }
                // ticket 추가.
                result.add(SaleTicketListItem.Item(SaleTicketItem(item.id, item.createAt, item)))

                // 그룹날짜를 바로 이전 날짜로 설정.
                groupHeaderDate = dateFormatUtil(item.createAt)
            } else {
                Timber.e("buyItem.date null point exception")
            }
        }
        return result
    }

    enum class TicketState(val option: Int) {
        SALE(0), SOLDOUT(2)
    }
}

data class SaleHistoryUiState(
    var list: List<SaleTicketListItem>? = emptyList(),
    val isLoading: Boolean? = true
) {
    val emptyState = (list.isNullOrEmpty() && isLoading == false)
}


data class SoldoutHistoryUiState(
    var list: List<SaleTicketListItem>? = emptyList(),
    val isLoading: Boolean? = true
) {
    val emptyState = (list.isNullOrEmpty() && isLoading == false)
}
