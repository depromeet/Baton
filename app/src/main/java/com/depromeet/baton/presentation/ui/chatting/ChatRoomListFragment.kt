package com.depromeet.baton.presentation.ui.chatting

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.depromeet.baton.R
import com.depromeet.baton.chat.ChatRoom
import com.depromeet.baton.databinding.FragmentChattingBinding
import com.depromeet.baton.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatRoomListFragment : BaseFragment<FragmentChattingBinding>(R.layout.fragment_chatting) {

    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var chatRoomAdapter: ChatRoomAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChatRoomRvAdapter()
    }

    private fun setChatRoomRvAdapter() {
        with(binding) {
            chatRoomAdapter = ChatRoomAdapter(::setRoomClickListener)
            rvChatList.adapter = chatRoomAdapter
        }

        //todo
        chatViewModel.chatRoomList.observe(viewLifecycleOwner) { chatRoomList ->
            chatRoomAdapter.submitList(chatRoomList)
        }
    }

    private fun setRoomClickListener(chatRoom: ChatRoom) {
        ChatRoomActivity.start(requireContext(), chatRoom)
    }
}