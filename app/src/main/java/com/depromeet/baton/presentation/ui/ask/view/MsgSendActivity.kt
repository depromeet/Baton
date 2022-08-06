package com.depromeet.baton.presentation.ui.ask.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivitySendMessageBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.WebActivity
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.ask.viewModel.MsgSendViewModel
import com.depromeet.baton.presentation.ui.ask.viewModel.SendMessageViewEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MsgSendActivity : BaseActivity<ActivitySendMessageBinding>(R.layout.activity_send_message) {
    val viewModel by viewModels<MsgSendViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
    }

    private fun setObserver() {

        viewModel.uiState.flowWithLifecycle(lifecycle)
            .onEach { binding.uiState = it }
            .launchIn(lifecycleScope)


        viewModel.viewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleTicketViewEvents)
            .launchIn(lifecycleScope)

    }

    private fun handleTicketViewEvents(viewEvents: List<SendMessageViewEvent >) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                is SendMessageViewEvent.EventBack -> {
                    onBackPressed()
                }
                is SendMessageViewEvent .EventUrlClick -> {
                    startActivity(WebActivity.start(this, viewEvent.address))
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }



    companion object {
        fun start(context: Context, messageId: Int) {
            val intent = Intent(context, MsgSendActivity::class.java)
            intent.putExtra("messageId", messageId)
            context.startActivity(intent)
        }
    }
}
