package com.depromeet.baton.presentation.ui.chatting

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.chat.*
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    //  private val chatRepository: ChatRepository
) : BaseViewModel() {


    private val publisher: RealTimeDataPublisher = MemoryRealTimeDataPublisher()
    private val chatRepository = ChatRepository(publisher)

    //todo ChatRoom정보: receiverId , senderName , receiverName
    // val userId = authRepository.authInfo?.userId!!
    private val chatRoom = ChatRoom(1, 2, "bean", "seungmin")
    val chatController = ChatController(chatRoom, chatRepository, viewModelScope)


    init {
        chatRepository.start()
        chatController.receiveMessages()
    }

    override fun onCleared() {
        super.onCleared()
        chatRepository.stop()
    }
}

class MemoryRealTimeDataPublisher : RealTimeDataPublisher {
    private val _objs = MutableSharedFlow<RealTimeData>(replay = 100)

    override suspend fun <T : RealTimeData> send(obj: T) {
        _objs.tryEmit(obj)
    }

    override fun receive(): Flow<RealTimeData> {
        return _objs
    }

}

