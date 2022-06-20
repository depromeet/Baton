package com.depromeet.baton.chat

import android.os.Parcelable
import android.text.Editable
import android.util.Log
import androidx.annotation.GuardedBy
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.ui.home.viewmodel.HomeViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

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

//todo 다빈: 채팅이력 ChatRoom 임시
@Parcelize
data class ChatRoom(
    val senderId: Int,
    val receiverId: Int,
    val senderName: String,
    val receiverName: String,
    val shopName: String? = null, //임시
    val LastMessage: String? = null, //임시
    val messageCount: Int? = null, //임시
    val receiverProfileImg:String?=null, //임시
    val howOld:Int?=null //임시
) : Parcelable

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

    //todo 다빈: test
    fun getMessages(room: ChatRoom): Flow<List<Message>> {
        return flow<List<Message>> {
            emit(
                listOf(
                    Message(1, State.DELIVERED, "이거 사줘줘하이하이하이하이하이하이줮줘주"),
                    Message(1, State.DELIVERED, "하이"),
                    Message(0, State.DELIVERED, "하하이하이이"),
                    Message(1, State.DELIVERED, "하하이하이하이하이하이이"),
                    Message(0, State.DELIVERED, "하이"),
                    Message(0, State.DELIVERED, "하하하이하이하이하이하이하이하이하이하이하이하이하이하이하이하이이하이하이이"),
                    Message(0, State.DELIVERED, "하이"),
                    Message(1, State.DELIVERED, "이거 사줘줘하이하이하이하이하이하이줮줘주"),
                    Message(1, State.DELIVERED, "하이"),
                    Message(0, State.DELIVERED, "하하이하이이"),
                    Message(1, State.DELIVERED, "하하이하이하이하이하이이"),
                    Message(1, State.DELIVERED, "이거 사줘줘하이하이하이하이하이하이줮줘주"),
                    Message(1, State.DELIVERED, "하이"),
                    Message(0, State.DELIVERED, "하하이하이이"),
                    Message(1, State.DELIVERED, "하하이하이하이하이하이이"),
                )
            )
        }
        /*    return synchronized(messageLock) {
                roomMessageStateFlowMap.getOrPut(room) { createDefaultMessageSharedFlow() }
            }*/
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

    //empty view 처리
    private val _emptyUiState = MutableLiveData<UIState>(UIState.NoData)
    val emptyUiState: LiveData<UIState> = _emptyUiState

    private val _uiState: MutableStateFlow<MessageUiState> = MutableStateFlow(createInitialState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun createInitialState(): MessageUiState {
        return MessageUiState(
            messages = listOf(),
            sendMessage = "",
            onSendMessageChanged = ::handleSendMessageChanged,
            sendMessageClick = ::handleSendMessageClick,
            seeMoreClick = ::handleSeeMoreDialogClick,
            turnOffNotificationClick = ::handleTurnOffNotificationClick,
            turnOnNotificationClick = ::handleTurnOnNotificationClick,
            leaveClick = ::handleLeaveClick
        )
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    private fun handleSendMessageClick() {
        addViewEvent(ViewEvent.SendMessage)
    }

    private fun handleSendMessageDone() {
        addViewEvent(ViewEvent.SendMessageDone)
    }

    private fun handleSeeMoreDialogClick() {
        addViewEvent(ViewEvent.OpenSeeMoreDialog)
    }

    private fun handleTurnOffNotificationClick() {
        addViewEvent(ViewEvent.TurnOffNotification)
    }

    private fun handleTurnOnNotificationClick() {
        addViewEvent(ViewEvent.TurnOnNotification)
    }

    private fun handleLeaveClick() {
        addViewEvent(ViewEvent.LeaveChatRoom)
    }

    private fun handleSendMessageChanged(editable: Editable?) {
        setMessage("$editable")
        _uiState.update { it.copy(sendMessage = "$editable") }
    }

    fun receiveMessages() {
        fun List<Message>.toMessageUiState(): MessageUiState {
            return MessageUiState(
                messages = this,
                currentMessage.get(),
                ::handleSendMessageChanged,
                ::handleSendMessageClick,
                ::handleSeeMoreDialogClick,
                ::handleTurnOffNotificationClick,
                ::handleTurnOnNotificationClick,
                ::handleLeaveClick
            )
        }

        chatRepository.getMessages(room)
            .map { it.toMessageUiState() }
            .onEach { _uiState.emit(it) }
            .launchIn(scope)

        if (_uiState.value.messages.isEmpty()) _emptyUiState.value = UIState.NoData
        else _emptyUiState.value = UIState.HasData
    }

    fun setMessage(message: String) {
        currentMessage.set(message)
    }

    /**
     * [currentMessage]를 채팅방에 보낸다.
     */
    fun send() {
        scope.launch {
            runCatching {
                fun makeMessage(): Message {
                    return Message(
                        senderId = room.senderId,
                        state = State.SENT,
                        message = currentMessage.get()
                    )
                }
                chatRepository.send(room, makeMessage())
            }
                .onSuccess {
                    setMessage("")
                    handleSendMessageDone()//layout 초기화
                }
                .onFailure {
                    Timber.e("메시지 전송 실패")
                }
        }
    }

    sealed interface ViewEvent {
        object SendMessage : ViewEvent
        object SendMessageDone : ViewEvent
        object OpenSeeMoreDialog : ViewEvent
        object TurnOffNotification : ViewEvent
        object TurnOnNotification : ViewEvent
        object LeaveChatRoom : ViewEvent
    }

    data class MessageUiState(
        val messages: List<Message>,
        val sendMessage: String?, //메시지
        val onSendMessageChanged: (Editable?) -> Unit, //메시지 바뀜
        val sendMessageClick: () -> Unit, //전송버튼 클릭
        val seeMoreClick: () -> Unit,//더보기 버튼 클릭
        val turnOffNotificationClick: () -> Unit, //알림끄기 버튼 클릭
        val turnOnNotificationClick: () -> Unit, //알림켜기 버튼 클릭
        val leaveClick: () -> Unit, //나가기 버튼 클릭
    ) {
        val isEnabled = sendMessage?.isNotBlank()
    }
}