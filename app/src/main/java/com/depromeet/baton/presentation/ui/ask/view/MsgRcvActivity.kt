package com.depromeet.baton.presentation.ui.ask.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.BatonApp
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityRcvMessageBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.WebActivity
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.ask.viewModel.MsgRcvViewModel
import com.depromeet.baton.presentation.ui.ask.viewModel.RcvMessageViewEvent
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MsgRcvActivity : BaseActivity<ActivityRcvMessageBinding>(R.layout.activity_rcv_message) {
    val viewModel by viewModels<MsgRcvViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
    }

    private fun setObserver(){

        viewModel.uiState.flowWithLifecycle(lifecycle)
            .onEach{binding.uiState = it}
            .launchIn(lifecycleScope)


        viewModel.viewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleTicketViewEvents)
            .launchIn(lifecycleScope)

    }

    private fun handleTicketViewEvents(viewEvents: List<RcvMessageViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
               is RcvMessageViewEvent.EventBack -> {
                    onBackPressed()
                }
               is RcvMessageViewEvent.EventCopy->{
                    createClipData(viewEvent.phoneNum)
                }
                is RcvMessageViewEvent.EventUrlClick -> {
                    startActivity(WebActivity.start(this, viewEvent.address))
                }
                is RcvMessageViewEvent.EventTicketClick ->{
                    TicketDetailActivity.start(this,viewEvent.id!!)
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }

    private fun createClipData(message: String) {
        val clipBoardManger: ClipboardManager = applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(BatonApp.TAG, message)
        clipBoardManger.setPrimaryClip(clipData)
        this@MsgRcvActivity.BdsToast("번호가 복사되었습니다").show()
    }

    companion object{
        fun start(context : Context, messageId : Int){
            val intent = Intent(context, MsgRcvActivity::class.java)
            intent.putExtra("messageId", messageId )
            context.startActivity(intent)
        }
    }
}