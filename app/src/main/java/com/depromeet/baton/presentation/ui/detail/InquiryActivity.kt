package com.depromeet.baton.presentation.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.depromeet.baton.R
import com.depromeet.baton.data.response.ResponseGetInquiryByTicket
import com.depromeet.baton.databinding.ActivityInquiryBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.ui.ask.view.MsgRcvActivity
import com.depromeet.baton.presentation.ui.chatting.ChatRoomActivity
import com.depromeet.baton.presentation.ui.chatting.ChatRoomAdapter
import com.depromeet.baton.presentation.ui.detail.viewModel.TicketDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InquiryActivity : BaseActivity<ActivityInquiryBinding>(R.layout.activity_inquiry) {
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private val ticketDetailViewModel by viewModels<TicketDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.ticketViewmodel = ticketDetailViewModel
        ticketDetailViewModel.getInquiryList()
        ticketDetailViewModel.getInquiryCount()
        setChatRoomRvAdapter()
        setClickListener()
    }

    private fun setClickListener(){
        with(binding){
            ticketDetailToolbar.setOnBackwardClick{
                onBackPressed()
            }
        }
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
        MsgRcvActivity.start(this@InquiryActivity,chatRoom.id)
    }

    companion object {
        fun start(context: Context, ticketId: Int) {
            val intent = Intent(context, InquiryActivity::class.java)
            intent.putExtra("ticketId", ticketId)
            context.startActivity(intent)
        }
    }
}