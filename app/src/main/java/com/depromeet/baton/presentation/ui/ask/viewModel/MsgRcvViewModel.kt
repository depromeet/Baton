package com.depromeet.baton.presentation.ui.ask.viewModel

import androidx.lifecycle.SavedStateHandle
import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MsgRcvViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle
) : BaseViewModel(){
    private val _uiState = MutableStateFlow(RcvMessageUiState( onBackClick = ::handleBackClick, onCopyClick = ::handleCopyClick, onUrlClick = ::handleUrlClick))
    val uiState get() = _uiState

    private val _viewEvents: MutableStateFlow<List<RcvMessageViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun getMessage(){
        val messageId : Int? = savedStateHandle.get<Int>("messageId")
        //Todo api 호출
    }

    private fun handleBackClick(){
        addViewEvent(RcvMessageViewEvent.EventBack)
    }

    private fun handleUrlClick(){
        addViewEvent(RcvMessageViewEvent.EventUrlClick(uiState.value!!.address?:""))
    }

    private fun handleCopyClick(){
        addViewEvent(RcvMessageViewEvent.EventCopy(uiState.value.phoneNumber?:""))
    }

    private fun addViewEvent(viewEvent: RcvMessageViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: RcvMessageViewEvent) {
        _viewEvents.update { it - viewEvent }
    }
}

data class RcvMessageUiState(
    val image : String ? ="",
    val gymName : String? ="석촌 어딘가",
    val address: String ? ="",
    val price : String ? ="20,000",
    val canNego : Boolean =true,
    val nickName: String? = "닉네임 여기",
    val phoneNumber: String? = "000000",
    var content : String = "무언가의 내용",
    val onBackClick : ()-> Unit,
    val onCopyClick :()->Unit,
    val onUrlClick :()->Unit
){
    val canNegoStr = if(canNego) "가격 제안 가능" else ""

}

sealed class  RcvMessageViewEvent {
    data class EventCopy(val phoneNum: String) : RcvMessageViewEvent()
    object EventBack : RcvMessageViewEvent()
    data class EventUrlClick(val address :String) : RcvMessageViewEvent()
}
