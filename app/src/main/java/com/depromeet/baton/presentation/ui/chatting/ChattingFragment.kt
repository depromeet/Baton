package com.depromeet.baton.presentation.ui.chatting

import android.os.Bundle
import android.view.View
import com.depromeet.baton.R
import com.depromeet.baton.chat.ChatRoom
import com.depromeet.baton.chat.Message
import com.depromeet.baton.chat.State
import com.depromeet.baton.databinding.FragmentChattingBinding
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChattingFragment : BaseFragment<FragmentChattingBinding>(R.layout.fragment_chatting) {

    private lateinit var chatListAdapter: ChatListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTicketItemRvAdapter()
    }

    //필터 아이템 어댑터
    private fun setTicketItemRvAdapter() {
        with(binding) {
            chatListAdapter = ChatListAdapter(::setChatClickListener)
            rvChatList.adapter = chatListAdapter
        }

/*        val senderId: Int,
        val receiverId: Int,
        val senderName: String,
        val receiverName: String,*/
        val list = listOf(
            ChatRoom(2, 1, "다빈", "가빈"),
        )

        //  chatViewModel.chatMessage.observe(this) { messageList->
        chatListAdapter.submitList(list)
        // }
    }

    private fun setChatClickListener(chatRoom: ChatRoom) {
        ChatRoomActivity.start(requireContext(), chatRoom)
    }
}