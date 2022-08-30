package com.depromeet.baton.presentation.ui.ask.viewModel

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.data.mapper.MsgMapper
import com.depromeet.baton.domain.model.Message
import com.depromeet.baton.domain.model.MsgType
import com.depromeet.baton.domain.repository.AskRepository
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

    val ints= Collections.nCopies(20,
        Message(1 , MsgType.SEND,null,"헬스","방배","닉네임","댜러댜러ㅑ더","1일전",false))

    private val _sendUiState: MutableStateFlow<AskSendUiState> =
        MutableStateFlow(AskSendUiState(emptyList()))
    val sendUiState = _sendUiState.asStateFlow()

    private val _recvUiState: MutableStateFlow<AskRecvUiState> =
        MutableStateFlow(AskRecvUiState(ints.toList()))
    val recvUiState = _recvUiState.asStateFlow()

    fun getRecvMsgList (){
        viewModelScope.launch {
            runCatching {
                askRepository.getRcvMsgList()
            }.onSuccess {
                    val list = it.data?.map{it-> MsgMapper.msgMapper(it,MsgType.RCV)}
                    if(list!=null) _recvUiState.update {it.copy(recvList = list) }
            }.onFailure { Timber.e(it.message) }

        }
    }
    fun getSendMsgList (){
        viewModelScope.launch {
            runCatching {
                askRepository.getSendMSgList()
            }.onSuccess {
                val list = it.data?.map{it-> MsgMapper.msgMapper(it,MsgType.SEND)}
                if(list!=null) _recvUiState.update {it.copy(recvList = list) }
            }.onFailure { Timber.e(it.message) }
        }
    }


    data class AskSendUiState(
        val sendList : List<Message>
    )

    data class AskRecvUiState(
        val recvList : List<Message>
    )

}

