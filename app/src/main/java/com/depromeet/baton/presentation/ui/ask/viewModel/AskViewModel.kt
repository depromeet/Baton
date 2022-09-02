package com.depromeet.baton.presentation.ui.ask.viewModel

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.data.mapper.MsgMapper
import com.depromeet.baton.domain.model.Message
import com.depromeet.baton.domain.model.MsgType
import com.depromeet.baton.domain.repository.AskRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AskViewModel @Inject constructor(
    private val askRepository: AskRepository
) : BaseViewModel(){

    private val _sendUiState: MutableStateFlow<AskSendUiState> =
        MutableStateFlow(AskSendUiState(emptyList()))
    val sendUiState = _sendUiState.asStateFlow()

    private val _recvUiState: MutableStateFlow<AskRecvUiState> =
        MutableStateFlow(AskRecvUiState(emptyList()))
    val recvUiState = _recvUiState.asStateFlow()


    fun getRecvMsgList (){
        viewModelScope.launch {
            runCatching {
                _recvUiState.update { it.copy(isLoading = true) }
                askRepository.getRcvMsgList()
            }.onSuccess {
                    when(it){
                        is NetworkResult.Success ->{
                            val list = it.data?.map{it-> MsgMapper.msgMapper(it,MsgType.RCV)}
                            if(list!=null) _recvUiState.update {it.copy(recvList = list ,isLoading = false ) }
                        }
                        is NetworkResult.Error->{
                            Timber.e(it.message)
                        }
                    }
            }.onFailure { Timber.e(it.message) }

        }
    }
    fun getSendMsgList (){
        viewModelScope.launch {
            runCatching {
                _sendUiState.update { it.copy(isLoading = true) }
                askRepository.getSendMsgList()
            }.onSuccess {
                when(it){
                    is NetworkResult.Success ->{
                        val list = it.data?.map{it-> MsgMapper.msgMapper(it,MsgType.SEND)}
                        if(list!=null) _sendUiState.update {it.copy(sendList = list , isLoading = false) }
                    }
                    is NetworkResult.Error->{
                        Timber.e(it.message)
                    }
                }
            }.onFailure { Timber.e(it.message) }
        }
    }


    data class AskSendUiState(
        val sendList : List<Message>,
        val isLoading : Boolean = true
    ){
        val emptyState = sendList.isNullOrEmpty() && !isLoading
    }

    data class AskRecvUiState(
        val recvList : List<Message>,
        val isLoading : Boolean = true
    ){
        val emptyState = recvList.isNullOrEmpty() && !isLoading
    }

}

