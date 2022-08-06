package com.depromeet.baton.presentation.ui.ask.viewModel

import androidx.lifecycle.SavedStateHandle
import com.depromeet.baton.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MsgSendViewModel  @Inject constructor(
    val savedStateHandle: SavedStateHandle
) : BaseViewModel(){
    private val _uiState = MutableStateFlow(SendMessageUiState( onBackClick = ::handleBackClick, onUrlClick = ::handleUrlClick))
    val uiState get() = _uiState

    private val _viewEvents: MutableStateFlow<List<SendMessageViewEvent >> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun getMessage(){
        val messageId : Int? = savedStateHandle.get<Int>("messageId")
        //Todo api 호출
    }

    private fun handleBackClick(){
        addViewEvent(SendMessageViewEvent.EventBack)
    }
    private fun handleUrlClick(){
        addViewEvent(SendMessageViewEvent.EventUrlClick(uiState.value!!.address?:""))
    }
    private fun addViewEvent(viewEvent: SendMessageViewEvent ) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: SendMessageViewEvent ) {
        _viewEvents.update { it - viewEvent }
    }
}

data class SendMessageUiState(
    val image : String ? ="",
    val gymName : String? ="석촌 어딘가",
    val address: String?="https://kimyk60.tistory.com/36",
    val price : String ? ="20,000",
    val canNego : Boolean =true,
    val nickName: String? = "닉네임 여기",
    var content : String = "무언가의 내용",
    val onBackClick : ()-> Unit,
    val onUrlClick :()->Unit
){
    val canNegoStr = if(canNego) "가격 제안 가능" else ""

}

sealed class  SendMessageViewEvent {
    data class EventUrlClick(val address: String) : SendMessageViewEvent()
    object EventBack : SendMessageViewEvent()
}
