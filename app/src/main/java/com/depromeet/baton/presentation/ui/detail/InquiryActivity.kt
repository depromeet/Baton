package com.depromeet.baton.presentation.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import com.depromeet.baton.R
import com.depromeet.baton.data.response.ResponseGetInquiryByTicket
import com.depromeet.baton.databinding.ActivityInquiryBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.ui.chatting.ChatRoomActivity
import com.depromeet.baton.presentation.ui.chatting.ChatRoomAdapter
import com.depromeet.baton.presentation.ui.detail.viewModel.TicketDetailViewModel

class InquiryActivity : BaseActivity<ActivityInquiryBinding>(R.layout.activity_inquiry) {
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private val ticketDetailViewModel by viewModels<TicketDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChatRoomRvAdapter()
        ticketDetailViewModel.getInquiryList()
    }

    private fun setChatRoomRvAdapter() {
        with(binding) {
            chatRoomAdapter = ChatRoomAdapter(::setRoomClickListener)
            rv.adapter = chatRoomAdapter
        }

        ticketDetailViewModel.inquiryList.observe(this) { inquiryList ->
            chatRoomAdapter.submitList(inquiryList)
        }
    }

    private fun setRoomClickListener(chatRoom: ResponseGetInquiryByTicket) {
        // ChatRoomActivity.start(this, chatRoom.ticket.id) todo 문의상세 페이지로 보내기
    }
}