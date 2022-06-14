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

class MemoryRealTimeDataPublisher : RealTimeDataPublisher {
    private val _objs = MutableSharedFlow<RealTimeData>(replay = 100)

    override suspend fun <T : RealTimeData> send(obj: T) {
        _objs.tryEmit(obj)
    }

    override fun receive(): Flow<RealTimeData> {
        return _objs
    }

}

class ChatTest : FeatureSpec({
    feature("채팅 기능") {
        val scope = CoroutineScope(Dispatchers.Unconfined)

        scenario("메시지를 보낸다.") {
            val chatRoom = ChatRoom(1, 2, "bean", "seungmin")
            val publisher: RealTimeDataPublisher = MemoryRealTimeDataPublisher()
            val chatRepository = ChatRepository(publisher)
            val chatController = ChatController(chatRoom, chatRepository, scope)

            chatRepository.start()

            publisher.receive()
                .onEach { println("publisher: $it") }
                .launchIn(scope)
            chatRepository.getMessages(chatRoom)
                .onEach { println("chat room: $it") }
                .launchIn(scope)

            chatController.run {
                receiveMessages()
                setMessage("hello world")
                send()
            }

            delay(100)

            val value = chatController.uiState.first()
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

            val b = roomB.uiState.first()
            b.messages.first() shouldBe messageForB
        }

        scenario("채팅을 끊을 수 있다.") {
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