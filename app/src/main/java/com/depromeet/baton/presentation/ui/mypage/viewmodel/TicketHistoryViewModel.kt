package com.depromeet.baton.presentation.ui.mypage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketItem
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketListItem
import kotlinx.coroutines.launch

class TicketHistoryViewModel : BaseViewModel() {
    private val ticketLiveData = MutableLiveData<List<SaleTicketListItem>>()
    val tasks: LiveData<List<SaleTicketListItem>> get() = ticketLiveData


    fun fetchSaleTicket() {
        viewModelScope.launch {
//            val listItems = repository.getHistory().toListItems()
//            ticketLiveData.postValue(listItems)
        }
    }

    // Repository 에서 가져온 리스트 가공. Repository에서는 날짜별로 정렬한 상태로 리스트를 반환해야함.
    //TODO entity로 변경할 것
    private fun List<SaleTicketItem>.toListItems(): List<SaleTicketListItem> {
        val result = arrayListOf<SaleTicketListItem>() // 결과를 리턴할 리스트
        var groupHeaderDate = "" // 그룹날짜
        this.forEachIndexed { index, ticket ->
            // 날짜가 달라지면 그룹헤더를 추가.
            if (groupHeaderDate !=  ticket.historyDate) {
                if(index != 0)result.add(SaleTicketListItem.Footer(ticket))
                result.add(SaleTicketListItem.Header(ticket))
            }
            // ticket 추가.
            result.add(SaleTicketListItem.Item( ticket))

            // 그룹날짜를 바로 이전 날짜로 설정.
            groupHeaderDate =  ticket.historyDate
        }

        return result
    }
}