package com.depromeet.baton.presentation.ui.mypage.viewmodel

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.model.TicketSimpleInfo
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
    private val userinfoRepository: UserinfoRepository
) : BaseViewModel(){
    private val _uiState: MutableStateFlow<SoldoutHistoryUiState> = MutableStateFlow(SoldoutHistoryUiState())
    val uiState = _uiState.asStateFlow()

    init{
        getSoldoutHistory(state = TicketState.SOLDOUT)
    }

    fun getSoldoutHistory(state: TicketState){
        val dummy= arrayListOf<TicketSimpleInfo>(
            TicketSimpleInfo(1,    "1장ㅈ",  "광진구 중곡동 텍스트 1234번지",200000,"https://depromeet11th.s3.ap-northeast-2.amazonaws.com/6team/18ee9e14-ceec-41a3-a458-97ec0d1099b1.jpeg","2022-06-02T03:34:23+09:00",
                2, tags=emptyList(), images=emptyList(),true,null,"2023-04-05",0.0,0.0,1000.0),
            TicketSimpleInfo(2,    "2장ㅈ",  "광진구 중곡동 텍스트 1234번지",200000,"https://depromeet11th.s3.ap-northeast-2.amazonaws.com/6team/18ee9e14-ceec-41a3-a458-97ec0d1099b1.jpeg","2022-06-05T03:34:23+09:00",
                2, tags=emptyList(), images=emptyList(),true,null,"2023-04-05",0.0,0.0,1000.0),
            TicketSimpleInfo(3,    "3장ㅈ",  "광진구 중곡동 텍스트 1234번지",200000,"https://depromeet11th.s3.ap-northeast-2.amazonaws.com/6team/18ee9e14-ceec-41a3-a458-97ec0d1099b1.jpeg","2022-06-05T03:34:23+09:00",
                2, tags=emptyList(), images=emptyList(),true,null,"2023-04-05",0.0,0.0,1000.0),
            TicketSimpleInfo(4,    "4장ㅈ",  "광진구 중곡동 텍스트 1234번지",200000,"https://depromeet11th.s3.ap-northeast-2.amazonaws.com/6team/18ee9e14-ceec-41a3-a458-97ec0d1099b1.jpeg","2022-06-06T03:34:23+09:00",
                2, tags=emptyList(), images=emptyList(),true,null,"2023-04-05",0.0,0.0,1000.0)
        )
        viewModelScope.launch {
            runCatching {
                userinfoRepository.getUserSellList(1,state.option)
            }.onSuccess {
                    res ->run{
                when(res){
                    is NetworkResult.Success<List<TicketSimpleInfo>> ->{
                        val list= res.data?.toListItems()
                        val dummys = dummy.toListItems()
                        _uiState.update { it.copy(list = dummys , isLoading = false) }
                    }
                    is NetworkResult.Error<List<TicketSimpleInfo>> ->{
                        Timber.e("call api "+res.message)
                    }
                 }
              }
            }.onFailure {
                    e -> Timber.e(e.toString()+"recall")
            }
        }
    }

    private fun List<TicketSimpleInfo>.toListItems(): List<SaleTicketListItem> {
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
        Timber.e("convert : "+result.toString())

        return result

    }

    enum class TicketState(val option: Int){
        SALE(0),SOLDOUT(2)
    }
}

/*
data class SoldoutHistoryUiState(
    var list : List<SaleTicketListItem> ?  = emptyList(),
    val isLoading : Boolean ? = true
){
    val emptyState  = (list.isNullOrEmpty()&& isLoading==false)
}
*/
