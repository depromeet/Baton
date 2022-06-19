package com.depromeet.baton.presentation.ui.chatting

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(

) : BaseViewModel() {


    private val _chatRoomUiState: MutableStateFlow<ChatRoomUiState> = MutableStateFlow(createChatRoomState())
    val chatRoomUiState = _chatRoomUiState.asStateFlow()


    private val _chatRoomViewEvents: MutableStateFlow<List<ChatRoomViewEvent>> = MutableStateFlow(emptyList())
    val chatRoomViewEvents = _chatRoomViewEvents.asStateFlow()

    private fun chatRoomAddViewEvent(viewEvent: ChatRoomViewEvent) {
        _chatRoomViewEvents.update { it + viewEvent }
    }

    fun chatRoomConsumeViewEvent(viewEvent: ChatRoomViewEvent) {
        _chatRoomViewEvents.update { it - viewEvent }
    }

    private fun handleSendMessageClick() {
        chatRoomAddViewEvent(ChatRoomViewEvent.OnSendMessage)
    }

    private fun handleSeeMoreDialogClick() {
        chatRoomAddViewEvent(ChatRoomViewEvent.OpenSeeMoreDialog)
    }

    private fun handleAlarmOffClick() {
        //todo
    }

    private fun handleExitClick() {
        //todo
    }

    private fun handleMessageChanged(editable: Editable?) {
        _chatRoomUiState.update { it.copy(message = editable.toString()) }
    }

    private fun createChatRoomState(): ChatRoomUiState {
        return ChatRoomUiState(
            message = "",
            onMessageChanged = ::handleMessageChanged,
            sendMessageClick = ::handleSendMessageClick,
            seeMoreClick = ::handleSeeMoreDialogClick,
            turnOffAlarmClick = ::handleAlarmOffClick,
            exitClick = ::handleExitClick
        )
    }

    sealed interface ChatRoomViewEvent {
        object OnSendMessage : ChatRoomViewEvent //메시지 전송
        object OpenSeeMoreDialog : ChatRoomViewEvent  //더보기 열림
    }

    data class ChatRoomUiState(
        val message: String, //메시지
        val onMessageChanged: (Editable?) -> Unit, //메시지 바뀜
        val sendMessageClick: () -> Unit, //전송버튼 클릭
        val seeMoreClick: () -> Unit, //더보기 버튼 클릭
        val turnOffAlarmClick: () -> Unit, //알림끄기 버튼 클릭
        val exitClick: () -> Unit, //나가기 버튼 클릭
    ) {
        val isEnabled = message.isNotBlank()
    }
}