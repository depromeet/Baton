package com.depromeet.baton.chat

import androidx.annotation.GuardedBy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

//BEAN: 리얼타임의 가시성이 애플리케이션 레이어까지 안올라 왔으면 좋겠다.
interface RealTimeData

//BEAN: start, stop 도 제공해야 하는거 아님?
// 애플리케이션 라이프 사이클에 맞춰서 동작할 필요가 있다.
interface RealTimeDataPublisher {
    /**
     * [RealTimeData]로 마크된 객체를 웹 소켓에 전송한다.
     */
    suspend fun <T : RealTimeData> send(obj: T)

    /**
     * [RealTimeData]로 마크된 객체 전부를 포함한 Flow를 반환한다.
     */
    fun receive(): Flow<RealTimeData>
}

// TODO: 프로토콜 명세 적용할 것
data class RealTimeMessage(
    val senderId: Int,
    val receiverId: Int,
    val senderName: String,
    val receiverName: String,
    val state: State,
    val message: String
) : RealTimeData {
}

enum class State {
    SENT,
    DELIVERED,
    READ
}

data class ChatRoom(
    val senderId: Int,
    val receiverId: Int,
    val senderName: String,
    val receiverName: String,
)

data class Message(
    val senderId: Int,
    val state: State,
    val message: String,
)

class ChatRepository(
    private val publisher: RealTimeDataPublisher,
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val messageLock = Any()

    @GuardedBy("messageLock")
    private val roomMessageStateFlowMap =
        mutableMapOf<ChatRoom, MutableSharedFlow<MutableList<Message>>>()

    private val counter = AtomicInteger(0)
    private var job: Job? = null

    fun start() {
        if (counter.getAndIncrement() == 0) {
            synchronized(this) { job = receiveRealTimeMessages() }
        }
    }

    fun stop() {
        if (counter.decrementAndGet() == 0) {
            synchronized(this) { job?.cancel() }
        }
    }

    private fun receiveRealTimeMessages(): Job {
        return publisher.receive()
            .filterIsInstance<RealTimeMessage>()
            .onEach {
                val message = it.getMessage()
                val chatRoom = it.getChatRoom()
                synchronized(messageLock) {
                    val roomMessageStateFlow = roomMessageStateFlowMap.getOrPut(chatRoom) {
                        createDefaultMessageSharedFlow()
                    }
                    val currentMessages =
                        roomMessageStateFlow.replayCache.firstOrNull() ?: mutableListOf()
                    currentMessages.add(message)
                    println("add currentMessage $message $currentMessages")
                    roomMessageStateFlow.tryEmit(currentMessages)
                }
            }
            .launchIn(scope)
    }

    fun getMessages(room: ChatRoom): Flow<List<Message>> {
        return synchronized(messageLock) {
            roomMessageStateFlowMap.getOrPut(room) { createDefaultMessageSharedFlow() }
        }
    }

    private fun createDefaultMessageSharedFlow(): MutableSharedFlow<MutableList<Message>> {
        return MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    }

    suspend fun send(room: ChatRoom, message: Message) {
        publisher.send(message.toRealTimeMessage(room))
    }

    private fun RealTimeMessage.getMessage(): Message {
        return Message(senderId, state, message)
    }

    private fun RealTimeMessage.getChatRoom(): ChatRoom {
        return ChatRoom(senderId, receiverId, senderName, receiverName)
    }

    private fun Message.toRealTimeMessage(room: ChatRoom): RealTimeMessage {
        return RealTimeMessage(
            senderId = room.senderId,
            receiverId = room.receiverId,
            senderName = room.senderName,
            receiverName = room.receiverName,
            state = state,
            message = message
        )
    }
}

//////////////////////// presentation layer

data class MessageUiState(
    val messages: List<String>
)

class ChatController(
    private val room: ChatRoom,
    private val chatRepository: ChatRepository,
    private val scope: CoroutineScope,
) {
    private val currentMessage: AtomicReference<String> = AtomicReference("")
    private val _uiState: MutableStateFlow<MessageUiState> = MutableStateFlow(createInitialState())
    val uiState = _uiState.asStateFlow()

    private fun createInitialState(): MessageUiState {
        return MessageUiState(emptyList())
    }

    fun receiveMessages() {
        fun List<Message>.toMessageUiState(): MessageUiState {
            // TODO: 어케 구현하누?
            return MessageUiState(this.map { it.message })
        }

        chatRepository.getMessages(room)
            .map { it.toMessageUiState() }
            .onEach { _uiState.emit(it) }
            .launchIn(scope)
    }

    fun setMessage(message: String) {
        currentMessage.set(message)
    }

    /**
     * [currentMessage]를 채팅방에 보낸다.
     */
    suspend fun send() {
        fun makeMessage(): Message {
            return Message(
                senderId = room.senderId,
                state = State.SENT,
                message = currentMessage.get()
            )
        }

        chatRepository.send(room, makeMessage())
        setMessage("")
    }

    fun leave() {}
    fun turnOffNotification() {}
    fun turnOnNotification() {}
}