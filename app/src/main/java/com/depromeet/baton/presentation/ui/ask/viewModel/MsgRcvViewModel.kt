package com.depromeet.baton.presentation.ui.ask.viewModel

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.R
import com.depromeet.baton.data.mapper.MsgMapper
import com.depromeet.baton.domain.model.MsgType
import com.depromeet.baton.domain.model.TradeType
import com.depromeet.baton.domain.repository.AskRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.util.RegexConstant
import com.depromeet.baton.presentation.util.getHyphenPhone
import com.depromeet.baton.presentation.util.priceFormat
import com.depromeet.baton.presentation.util.uriConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MsgRcvViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val askRepository: AskRepository
) : BaseViewModel(){
    private val _uiState = MutableStateFlow(RcvMessageUiState( onBackClick = ::handleBackClick, onCopyClick = ::handleCopyClick, onUrlClick = ::handleUrlClick))
    val uiState get() = _uiState

    private val _viewEvents: MutableStateFlow<List<RcvMessageViewEvent>> = MutableStateFlow(emptyList())
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
                                    price= priceFormat(it.data!!.ticketResponse.price.toFloat())+"원",
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
        addViewEvent(RcvMessageViewEvent.EventBack)
    }

    private fun handleUrlClick(){
        addViewEvent(RcvMessageViewEvent.EventUrlClick(
            "https://map.naver.com/v5/search/${uiState.value!!.address!!.replace(" ","")}"))
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
    val image : String? = null,
    val gymName : String? ="",
    val type : String?="",
    val status : String?= "삭제됨",
    val address: String ? ="",
    val price : String ? ="",
    val canNego : Boolean =false,
    val tradeType: String?=null,
    val nickName: String? = "",
    val phoneNumber: String? = "",
    var content : String = "",
    val onBackClick : ()-> Unit,
    val onCopyClick :()->Unit,
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

sealed class  RcvMessageViewEvent {
    data class EventCopy(val phoneNum: String) : RcvMessageViewEvent()
    object EventBack : RcvMessageViewEvent()
    data class EventUrlClick(val address :String) : RcvMessageViewEvent()
}
