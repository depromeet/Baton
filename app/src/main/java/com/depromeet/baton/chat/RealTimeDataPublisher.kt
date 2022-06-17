package com.depromeet.baton.chat

import androidx.annotation.GuardedBy
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.presentation.base.BaseViewModel
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
//서버에서 준 스펙 이거고 이 정보 보내면 소켓에 실러 보내겠다고 했는데 우리는 이 정보를 기존에 알 방법 없음
//이걸 받아온다는 거 정도만 알 수 있고 기타 커뮤니케이션 모름
//얘네는 소켓 레벨에서 관리하는 레벨
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

//ChatRoom은 서버 스펙에 맞춰 갈거임 이런거 다 서버쪽에서 주긴 할텐데
//그 전에 얘네를 받아오는 api가 없어서 이부분 고민 필요
//보내는,받는이의 아이디를 통해 채팅룸은 분리 가능
//채팅룸이랑 메시지를 화면에 필요한 코드로 mapping하는 코드를 넣어도 됨
//레포지토리 레벨에서 관리하는 객체들이라고 생각
//나는 ChatRoom이랑 flow로 받아오는 Message로 ui그리면 됨
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

//가령,
/*class ChatViewModel : BaseViewModel() {
    fun init() {
        start()
    }

    override fun onCleared() {
        super.onCleared()
        stop()
    }
}*/

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

    //레포지토리 사용시 start호출하고 끝날때 stop호출해라
    //웹소켓 받아오는 일을 종료하고 싶어서 이건 그냥 규약처럼 써라
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

    //메시지 받아오고 싶을때->이거 호출만 하면 전체 메시지가 플로우로 계속 emit될것
    fun getMessages(room: ChatRoom): Flow<List<Message>> {
        return synchronized(messageLock) {
            roomMessageStateFlowMap.getOrPut(room) { createDefaultMessageSharedFlow() }
        }
    }

    private fun createDefaultMessageSharedFlow(): MutableSharedFlow<MutableList<Message>> {
        return MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    }

    //메시지 받고 싶을때->채팅방정보와 메시지를 보내면 얘가 알아서 소켓에 쏠거임
    //사용자가 전송 누를때 이 함수 호출
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
    val messages: List<Message>
)

/*class ChatViewModel(
    private val room:ChatRoom,
    private val chatRepository: ChatRepository
):BaseViewModel(){

//화면 그릴때 이런식으로 받아서 유아이 스테이트 기반으로 ChatController uiState얘가 계속 emit되는거를 받아서 수동적으로
뷰를 그리는걸 짜면됨
뷰를 짜다보면 uistate가 어때야해,, 라는건 뷰를 짜봐야 안다
class UiState(){..}  아이콘이나 시간, 누가 보냈는지 등을 고민해야한다 지금 데이터 스펙에 없으면 말하고,,
    val chatController=ChatController(room,chatRepository,viewModelScope)
}*/

//ui에서 쓸수 있을꺼라고 생각한 공통 모듈을 만들어둔것
//위에까지는 repository내용인거고 일종의 ChatViewModel내부에서 구현해야할 내용은 얘네인거임 어떻게 보면,,
//ChatViewModel에서 채팅방 나가기, 알림끄기 키기 ,메시지 보내기, 메시지 플로우 받아오기는
//채팅쓰는 어떤 화면에서도 쓰니가 뷰모델 없이도 구현할 수 있도록 이걸 만든것
//뷰모델이 하는 행동들, 즉 뷰이벤트를 받아서 어떻게 처리할건지에 대한 내용의구현테들이 여기 들어있음
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
            //return MessageUiState(this.map { it.message })  //이걸 원하는 ui state로 바꿔서 사용해도도딤
            return MessageUiState(this) //id를 담고 있는 메시지 객체 그 자체로 넘김
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