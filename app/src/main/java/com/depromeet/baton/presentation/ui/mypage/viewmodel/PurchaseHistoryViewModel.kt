package com.depromeet.baton.presentation.ui.mypage.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.data.response.UserBuyListResponse
import com.depromeet.baton.domain.repository.UserinfoRepository
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketItem
import com.depromeet.baton.presentation.ui.mypage.model.SaleTicketListItem
import com.depromeet.baton.presentation.util.dateFormatUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(
    private val userinfoRepository: UserinfoRepository
) :BaseViewModel(){
    private val _uiState: MutableStateFlow<PurchaseHistoryUiState> = MutableStateFlow(PurchaseHistoryUiState())
    val uiState = _uiState.asStateFlow()

    init{
        getHistory()
    }

    private fun getHistory(){
        viewModelScope.launch {
           runCatching {
                userinfoRepository.getUserBuyList(1)
            }.onSuccess {
                res ->
                    val list= res.data?.toListItems()
                    _uiState.update { it.copy(list = list , isLoading = false) }

           }.onFailure {
               e -> Timber.e(e.toString())
           }
        }
    }

    private fun List<UserBuyListResponse>.toListItems(): List<SaleTicketListItem> {
        val result = arrayListOf<SaleTicketListItem>() // 결과를 리턴할 리스트
        var groupHeaderDate = "" // 그룹날짜
        this.forEachIndexed { index, item->
            // 날짜가 달라지면 그룹헤더를 추가.
            if(item.date != null) {
                if (groupHeaderDate !=  dateFormatUtil(item.date)) {
                    if(index != 0) result.add(SaleTicketListItem.Footer(SaleTicketItem(item.buyIdx,item.date,item.ticket)))
                    result.add(SaleTicketListItem.Header(SaleTicketItem(item.buyIdx,item.date,item.ticket)))
                }
                // ticket 추가.
                result.add(SaleTicketListItem.PurchasedItem(SaleTicketItem(item.buyIdx,item.date,item.ticket)))

                // 그룹날짜를 바로 이전 날짜로 설정.
                groupHeaderDate = dateFormatUtil(item.date)
            }else{
                Timber.e("buyItem.date null point exception")
            }
        }
        return result
    }
}

data class PurchaseHistoryUiState(
    var list : List<SaleTicketListItem> ?  = emptyList(),
    val isLoading : Boolean ? = true
){
    val emptyState  = (list.isNullOrEmpty()&& isLoading==false) //View.GONE else View.VISIBLE
}
