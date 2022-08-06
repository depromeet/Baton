package com.depromeet.baton.presentation.ui.ask.viewModel

import com.depromeet.baton.domain.model.Message
import com.depromeet.baton.domain.model.MsgType
import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AskViewModel @Inject constructor() : BaseViewModel(){

    val ints= Collections.nCopies(20,
        Message(1 , MsgType.SEND,null,"헬스","방배","닉네임","댜러댜러ㅑ더","1일전",false))

    private val _sendUiState: MutableStateFlow<AskSendUiState> =
        MutableStateFlow(AskSendUiState(ints.toList()))
    val sendUiState = _sendUiState.asStateFlow()

    private val _recvUiState: MutableStateFlow<AskRecvUiState> =
        MutableStateFlow(AskRecvUiState(ints.toList()))
    val recvUiState = _recvUiState.asStateFlow()

    data class AskSendUiState(
        val sendList : List<Message>
    )

    data class AskRecvUiState(
        val recvList : List<Message>
    )

}

