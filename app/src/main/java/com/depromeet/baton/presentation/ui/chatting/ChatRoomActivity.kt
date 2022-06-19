package com.depromeet.baton.presentation.ui.chatting

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.chat.Message
import com.depromeet.baton.chat.State
import com.depromeet.baton.databinding.ActivityChatRoomBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.util.getDateDay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ChatRoomActivity : BaseActivity<ActivityChatRoomBinding>(R.layout.activity_chat_room) {

    private lateinit var chatMessageAdapter: ChatMessageAdapter
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = chatViewModel
        initLayout()
        setObserve()
        setTicketItemRvAdapter()
    }

    private fun setObserve() {
        chatViewModel.chatRoomViewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleViewEvents)
            .launchIn(lifecycleScope)

        chatViewModel.chatRoomUiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(lifecycleScope)
    }

    private fun initLayout() {
        val date = Date(System.currentTimeMillis())
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale("ko", "KR"))
        val strDate = sdf.format(date)
        binding.tvDate.text = "$strDate (${getDateDay(strDate)})"
    }


    private fun handleViewEvents(viewEvents: List<ChatViewModel.ChatRoomViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {

            }
            chatViewModel.chatRoomConsumeViewEvent(viewEvent)
        }
    }

    //필터 아이템 어댑터
    private fun setTicketItemRvAdapter() {
        with(binding) {
            chatMessageAdapter = ChatMessageAdapter(1) //유저 아이디
            rvChat.adapter = chatMessageAdapter
        }

        val list = listOf(
            Message(1, State.DELIVERED, "이거 사줘줘하이하이하이하이하이하이줮줘주"),
            Message(1, State.DELIVERED, "하이"),
            Message(0, State.DELIVERED, "하하이하이이"),
            Message(1, State.DELIVERED, "하하이하이하이하이하이이"),
            Message(0, State.DELIVERED, "하이"),
            Message(0, State.DELIVERED, "하하하이하이하이하이하이하이하이하이하이하이하이하이하이하이하이이하이하이이"),
            Message(0, State.DELIVERED, "하이")
        )

        //  chatViewModel.chatMessage.observe(this) { messageList->
        chatMessageAdapter.submitList(list)
        // }
    }
}