package com.depromeet.baton.presentation.ui.mypage.viewmodel

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.model.MypageTicketResponse
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.UserinfoRepository
import com.depromeet.baton.map.util.NetworkResult
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
class SoldoutHistoryViewModel @Inject constructor(
    private val userinfoRepository: UserinfoRepository,
    private val authRepository: AuthRepository
) : BaseViewModel(){
    private val _uiState: MutableStateFlow<SoldoutHistoryUiState> = MutableStateFlow(SoldoutHistoryUiState())
    val uiState = _uiState.asStateFlow()

    init{
        getSoldoutHistory(state = TicketState.SOLDOUT)
    }

    fun getSoldoutHistory(state: TicketState){
        viewModelScope.launch {
            runCatching {
                userinfoRepository.getUserSellList(authRepository.authInfo!!.userId,state.option)
            }.onSuccess {
                    res ->run{
                when(res){
                    is NetworkResult.Success<List<MypageTicketResponse>> ->{
                        val list= res.data?.toListItems()
                        _uiState.update { it.copy(list = list , isLoading = false) }
                    }
                    is NetworkResult.Error<List<MypageTicketResponse>> ->{
                        Timber.e(res.message)
                    }
                 }
              }
            }.onFailure {
                    e -> Timber.e(e.toString())
            }
        }
    }

    private fun List<MypageTicketResponse>.toListItems(): List<SaleTicketListItem> {
        val result = arrayListOf<SaleTicketListItem>() // 결과를 리턴할 리스트
        var groupHeaderDate = "" // 그룹날짜
        this.forEachIndexed { index, item->
            // 날짜가 달라지면 그룹헤더를 추가.
            if(item.createAt != null) {
                if (groupHeaderDate !=  dateFormatUtil(item.createAt)) {
                    if(index != 0) result.add(SaleTicketListItem.Footer(SaleTicketItem(item.id,item.createAt,item)))
                    result.add(SaleTicketListItem.Header(SaleTicketItem(item.id,item.createAt,item)))
                }
                // ticket 추가.
                result.add(SaleTicketListItem.Item(SaleTicketItem(item.id,item.createAt,item)))

                // 그룹날짜를 바로 이전 날짜로 설정.
                groupHeaderDate = dateFormatUtil(item.createAt)
            }else{
                Timber.e("buyItem.date null point exception")
            }
        }
        return result

    }

    enum class TicketState(val option: Int){
        SALE(0),SOLDOUT(2)
    }
}

