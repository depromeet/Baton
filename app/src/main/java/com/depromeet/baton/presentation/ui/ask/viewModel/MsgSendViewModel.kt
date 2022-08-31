package com.depromeet.baton.presentation.ui.ask.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.repository.AskRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.util.getHyphenPhone
import com.depromeet.baton.presentation.util.priceFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MsgSendViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val askRepository: AskRepository
) : BaseViewModel(){
    private val _uiState = MutableStateFlow(SendMessageUiState( onBackClick = ::handleBackClick, onUrlClick = ::handleUrlClick))
    val uiState get() = _uiState

    private val _viewEvents: MutableStateFlow<List<SendMessageViewEvent >> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    init {
        getMessage()
    }

    private fun getMessage(){
        val messageId : Int? = savedStateHandle.get<Int>("messageId")
        viewModelScope.launch {
            runCatching {
                askRepository.getMsgDetail(messageId!!)
            }
                .onSuccess {
                    when(it){
                        is NetworkResult.Success ->{
                            if(it.data!=null){
                                _uiState.update { ui -> ui.copy(
                                    type=it.data!!.ticketResponse.type,
                                    tradeType = it.data!!.ticketResponse.tradeType,
                                    gymName = it.data!!.ticketResponse.location,
                                    price= priceFormat(it.data!!.ticketResponse.price.toFloat()) +"원",
                                    canNego = it.data!!.ticketResponse.canNego,
                                    nickName = it.data!!.user.nickname,
                                    phoneNumber = getHyphenPhone(it.data!!.user.phoneNumber.toString()),
                                    content = it.data!!.content
                                ) }
                            }

                        }
                        is NetworkResult.Error->{
                            Timber.e(it.message)
                        }
                    }
                }
                .onFailure {
                    Timber.e(it.message)
                }

        }
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
    val image : String? = null,
    val gymName : String? ="",
    val type : String?="",
    val status : String? ="거래완료",
    val address: String?="",
    val price : String ? ="",
    val canNego : Boolean =false,
    val tradeType: String?=null,
    val phoneNumber: String? = "",
    val nickName: String? = "",
    var content : String = "",
    val onBackClick : ()-> Unit,
    val onUrlClick :()->Unit
){
    val canNegoStr = if(canNego) "가격 제안 가능" else ""
    val tradeStr
            = when(tradeType){
        "UNTECT" -> {" · 선결제 가능"}
        "CONTECT" ->{" · 현장거래 가능"}
        else -> {" · 현장거래/선결제 가능"}
    }
    val urlColor = if(status=="삭제됨") com.depromeet.bds.R.color.gy60 else com.depromeet.bds.R.color.gy80

}

sealed class  SendMessageViewEvent {
    data class EventUrlClick(val address: String) : SendMessageViewEvent()
    object EventBack : SendMessageViewEvent()
}
