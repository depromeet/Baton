package com.depromeet.baton.presentation.ui.detail.viewModel

import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TicketDetailBottomViewModel@Inject constructor():BaseViewModel() {
    private val _uiState = MutableStateFlow(BottomUiState(DetailBottomOption.BUYER,false))
    val uiState = _uiState.asStateFlow()

    fun  setBottomMenu(option: DetailBottomOption){
        _uiState.update { it.copy(option = option) }
    }

    fun updateBottomUistate(isOwner: Boolean){
        _uiState.update { it.copy(isOwner=isOwner) }
    }

    data class BottomUiState(
        val option : DetailBottomOption,
        val isOwner :Boolean
    ){
        val menuList = when(option){
            DetailBottomOption.SELLER-> sellerOption
            DetailBottomOption.STATUS -> statusOption
            DetailBottomOption.BUYER -> buyerOption
            DetailBottomOption.REPORT-> reportOption
        }
    }

    companion object{
        val sellerOption = arrayListOf("판매 상태 변경하기","삭제하기") //TODO 수정하기
        val buyerOption = arrayListOf("신고하기")
        val statusOption = arrayListOf("판매중","예약중","거래완료")
        val reportOption = arrayListOf("사칭/사기","상업적 광고 및 판매","게시판 성격에 부적절함","낚시/도배")
    }
}

enum class DetailBottomOption(val title: String){
    SELLER("글 메뉴"), BUYER("글메뉴"),STATUS("상태 변경"),REPORT("신고사유 선택")
}


