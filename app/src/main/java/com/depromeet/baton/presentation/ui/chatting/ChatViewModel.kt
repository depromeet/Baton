package com.depromeet.baton.presentation.ui.chatting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.chat.*
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    //  private val chatRepository: ChatRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    private val publisher: RealTimeDataPublisher = MemoryRealTimeDataPublisher()
    private val chatRepository = ChatRepository(publisher)

    private val _chatRoomList = MutableLiveData<List<ChatRoom>>()
    val chatRoomList: LiveData<List<ChatRoom>> = _chatRoomList

    /** 채팅 이력 가져오기 */
    init {
        getChatRoomList()
    }

    /** 채팅 이력 리스트에서 선택한 채팅방 */
    var chatRoom = savedStateHandle.get<ChatRoom>(ChatRoomActivity.GO_CHAT_ROOM_KEY)
        ?: ChatRoom(authRepository.authInfo?.userId!!, 2, "", "")
    val chatController = ChatController(chatRoom, chatRepository, viewModelScope)

    /** 채팅 이력 리스트에서 선택한 채팅방에서 채팅 시작 */
    fun startChat() {
        chatRepository.start()
        chatController.receiveMessages()
    }

    //todo 채팅이력 가져오기 api
    private fun getChatRoomList() {
        viewModelScope.launch {
            runCatching {

                //todo  ChatRoom list 임시
                _chatRoomList.value = listOf(
                    ChatRoom(
                        senderId = 1,
                        receiverId = 2,
                        receiverProfileImg = "https://i1.sndcdn.com/artworks-QM9GPYfLAnWwxuzb-3SPsFA-t240x240.jpg",
                        senderName = "다빈",
                        receiverName = "가빈",
                        shopName = "다이어트는포토샵으로헬스장",
                        LastMessage = "팔아유?",
                        messageCount = 1,
                        howOld = 7
                    ),
                    ChatRoom(
                        senderId = 1,
                        receiverId = 2,
                        receiverProfileImg = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVMnQh-iAYOSk64vM3Xn2ZPI776Lj0_Jzk3Mkjoj_GOBy1HZZ0Uz8gTEIOASUsfvjmSNQ&usqp=CAU",
                        senderName = "다빈",
                        receiverName = "룰루랄라빈",
                        shopName = "으라차차헬스장",
                        LastMessage = "살까유?",
                        messageCount = 18,
                        howOld = 11
                    )
                )

                //todo
                /* chatRepository.getChatRoom()
               when (it) {
                   is UIState.Success<*> -> {
                   //  _chatRoomList.value  = it.data as List<ChatRoom>
                   }
               }*/
            }.onSuccess {
            }.onFailure {
                Timber.e(it.toString())
            }
        }
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

