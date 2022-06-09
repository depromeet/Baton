package com.depromeet.baton.presentation.ui.detail.viewModel

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketMoreViewModel @Inject constructor():BaseViewModel(){
    private val _uiState = MutableStateFlow<List<FilteredTicket>>(emptyList())
    val uiState = _uiState.asStateFlow()

    private val _networkState = MutableStateFlow<TicketMoreNetwork>(TicketMoreNetwork.Loading)
    val networkState = _networkState.asStateFlow()

    init{
        viewModelScope.launch {
            runCatching {

                return@runCatching initState()
            }.onSuccess {
                //Api result
                data -> _uiState.update { data }
            }.onFailure {
                error ->_networkState.update { TicketMoreNetwork.Failure(error.message.toString()) }
            }
        }
    }


    private fun initState(): List<FilteredTicket>{
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