package com.depromeet.baton.presentation.ui.detail.viewModel

import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class TicketDetailBottomViewModel:BaseViewModel() {
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
        val sellerOption = arrayListOf("")
        val buyerOption = arrayListOf("")
        val statusOption = arrayListOf("")
        val reportOption = arrayListOf("")
    }
}

enum class DetailBottomOption(val title: String){
    SELLER("글 메뉴"), BUYER("신고하기"),STATUS("상태 변경"),REPORT("상태 변경")
}


