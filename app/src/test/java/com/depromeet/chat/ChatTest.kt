package com.depromeet.chat

import com.depromeet.baton.chat.*
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.junit.Ignore

class MemoryRealTimeDataPublisher : RealTimeDataPublisher {
    private val _objs = MutableSharedFlow<RealTimeData>(replay = 100)

    override suspend fun <T : RealTimeData> send(obj: T) {
        _objs.tryEmit(obj)
    }

    override fun receive(): Flow<RealTimeData> {
        return _objs
    }

}
@Ignore
class ChatTest : FeatureSpec({
    feature("채팅 기능") {
        val scope = CoroutineScope(Dispatchers.Unconfined)

        scenario("메시지를 보낸다.") {
            val chatRoom = ChatRoom(1, 2, "bean", "seungmin")
            val publisher: RealTimeDataPublisher = MemoryRealTimeDataPublisher()
            val chatRepository = ChatRepository(publisher)
            //chatController를 사용자, 유저로 생각할수있음 뷰 이벤트 받ㅇ서 다 처리하는 애니까
            val chatController = ChatController(chatRoom, chatRepository, scope)

            chatRepository.start()

            publisher.receive()
                .onEach { println("publisher: $it") }
                .launchIn(scope)
            chatRepository.getMessages(chatRoom)
                .onEach { println("chat room: $it") }
                .launchIn(scope)

            chatController.run {
                receiveMessages() //화면에 그림 그리기를 의도함
                setMessage("hello world") //사용자가 메시지 채팅후
                send() //사용자가 메시지를 보낸다
            }

            delay(100)

            val value = chatController.uiState.first() //그리고 나면 챗 컨트롤러의 유아이 스테이트가 사용자가 보낸 메시지로 채져있을것
            value.messages.first() shouldBe "hello world"
        }

        scenario("채팅방에 여러 메시지를 보낼 수 있다.") {
            val chatRoom = ChatRoom(1, 2, "bean", "seungmin")
            val publisher: RealTimeDataPublisher = MemoryRealTimeDataPublisher()
            val chatRepository = ChatRepository(publisher)
            val chatController = ChatController(chatRoom, chatRepository, scope)

            chatRepository.start()

            val message1 = "hello world"
            val message2 = "multiple messages are allowed"
            val message3 = "hello fellow."

            chatController.run {
                receiveMessages()
                setMessage(message1)
                send()
                setMessage(message2)
                send()
                setMessage(message3)
                send()
            }

            delay(100)

            chatController.uiState.value.messages shouldBe listOf(message1, message2, message3)
        }

        scenario("채팅방 분리되어서 보낼 수 있다.") {
            val chatRoomA = ChatRoom(1, 2, "bean", "seungmin")
            val chatRoomB = ChatRoom(1, 3, "bean", "depro")
            val publisher: RealTimeDataPublisher = MemoryRealTimeDataPublisher()
            val chatRepository = ChatRepository(publisher)
            val roomA = ChatController(chatRoomA, chatRepository, scope)
            val roomB = ChatController(chatRoomB, chatRepository, scope)

            chatRepository.start()

            val messageForA = "this message for A"
            roomA.run {
                receiveMessages()
                setMessage(messageForA)
                send()
            }

            val messageForB = "this message for B"
            roomB.run {
                receiveMessages()
                setMessage(messageForB)
                send()
            }

            delay(100)

            val a = roomA.uiState.first()
            a.messages.first() shouldBe messageForA

            //룸 B에 보낸건 룸B에만 있고,,
            val b = roomB.uiState.first()
            b.messages.first() shouldBe messageForB
        }

        scenario("채팅을 끊을 수 있다.") {
            val chatRoom = ChatRoom(1, 2, "bean", "seungmin")
            val publisher: RealTimeDataPublisher = MemoryRealTimeDataPublisher()
            val chatRepository = ChatRepository(publisher)
            val chatController = ChatController(chatRoom, chatRepository, scope)

            chatRepository.start()

            //이런 식으로 메시지가 순차적으로 들어온다고 했을때 이 3개가 레포지토리에 들어오면
            //chatController의 state가 메시지를 전부 확인할 수 있는 형태로 emit될거다
            val message1 = "hello world"
            val message2 = "multiple messages are allowed"
            val message3 = "hello fellow."

            chatController.run {
                receiveMessages()
                setMessage(message1)
                send()
                setMessage(message2)
                send()

                // 중간에 멈춘다고 했을 때
                chatRepository.stop()

                setMessage(message3)
                send()
            }

            delay(100)

            chatController.uiState.value.messages shouldNotBe listOf(message1, message2, message3)
            chatController.uiState.value.messages shouldBe listOf(message1, message2)
        }
    }
})