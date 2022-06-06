package com.depromeet.baton.presentation.ui.detail.viewModel

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.R
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.home.view.TicketItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketMoreViewModel @Inject constructor():BaseViewModel(){
    private val _uiState = MutableStateFlow<List<TicketItem>>(emptyList())
    val uiState = _uiState.asStateFlow()

    private val _networkState = MutableStateFlow<TicketMoreNetwork>(TicketMoreNetwork.Loading)
    val networkState = _networkState.asStateFlow()

    init{
        viewModelScope.launch {
            runCatching {

            }.onSuccess {
                //Api result
                _uiState.update { initState() }
            }.onFailure {
                error -> _networkState.update { TicketMoreNetwork.Failure(error.message.toString()) }
            }
        }
    }


    private fun initState():List<TicketItem>{
        //Api
        return  arrayListOf(
            TicketItem("테리온 휘트니스 당산점", "기타", "100,000원", "30일 남음", "영등포구 양평동", "12m", R.drawable.dummy4),
            TicketItem("진휘트니스 양평점", "헬스", "3,000원", "60일 남음", "광진구 중곡동", "12m", R.drawable.dummy3),
            TicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy2),
            TicketItem("바톤휘트니스 대왕점", "헬스", "19,000원", "5일 남음", "광진구 중곡동", "12m", R.drawable.dummy1),
            TicketItem("휴메이크 휘트니스 석촌점", "필라테스", "223,000원", "4일 남음", "광진구 중곡동", "12m", R.drawable.dummy5),
        )
    }

    sealed class TicketMoreNetwork(){
        object  Success : TicketMoreNetwork()
        data class Failure(val msg : String) : TicketMoreNetwork()
        object Loading : TicketMoreNetwork()
    }
}