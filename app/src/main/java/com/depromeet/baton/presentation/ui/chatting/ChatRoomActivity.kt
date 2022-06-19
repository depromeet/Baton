package com.depromeet.baton.presentation.ui.chatting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.chat.ChatController
import com.depromeet.baton.chat.ChatRoom
import com.depromeet.baton.databinding.ActivityChatRoomBinding
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.util.getDateDay
import com.depromeet.baton.presentation.util.isScrollable
import com.depromeet.baton.presentation.util.setStackFromEnd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ChatRoomActivity : BaseActivity<ActivityChatRoomBinding>(R.layout.activity_chat_room) {

    private val chatViewModel: ChatViewModel by viewModels()
    private lateinit var chatMessageAdapter: ChatMessageAdapter
    private var isKeyboardOpen = false // 키보드 올라왔는지 확인

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewModel = chatViewModel

        initLayout()
        setObserve()
        setRvAdapter()
        setRvScrollPosition()
        setBackBtnOnClickListener()
    }

    private fun setObserve() {
        chatViewModel.chatController.viewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleViewEvents)
            .launchIn(lifecycleScope)

        chatViewModel.chatController.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(lifecycleScope)
    }

    private fun setRvAdapter() {
        chatMessageAdapter = ChatMessageAdapter(1)
        binding.rvChat.adapter = chatMessageAdapter // todo val userId = authRepository.authInfo?.userId!!
    }

    @SuppressLint("SetTextI18n")
    private fun initLayout() {
        val date = Date(System.currentTimeMillis())
        val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale("ko", "KR"))
        val strDate = simpleDateFormat.format(date)
        binding.tvDate.text = "$strDate (${getDateDay(strDate)})"
    }

    private fun handleViewEvents(viewEvents: List<ChatController.ViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                ChatController.ViewEvent.SendMessage -> chatViewModel.chatController.send()
                ChatController.ViewEvent.SendMessageDone -> binding.etChat.text = Editable.Factory.getInstance().newEditable("")
                ChatController.ViewEvent.OpenSeeMoreDialog -> {} //todo
                ChatController.ViewEvent.TurnOffNotification -> {} //todo
                ChatController.ViewEvent.TurnOnNotification -> {} //todo
                ChatController.ViewEvent.LeaveChatRoom -> {} //todo
            }
            chatViewModel.chatController.consumeViewEvent(viewEvent)
        }
    }

    private fun setBackBtnOnClickListener() {
        binding.bdsBackwardAppbar.setOnBackwardClick { onBackPressed() }
    }

    private fun setRvScrollPosition() {
        val onLayoutChangeListener =
            View.OnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
                // 키보드가 올라와 높이가 변함
                if (bottom < oldBottom) {
                    binding.rvChat.scrollBy(0, oldBottom - bottom) // 스크롤 유지를 위해 추가
                }
            }


        binding.apply {
            rvChat.scrollToPosition(chatMessageAdapter.itemCount - 1)  //최초 진입 시 스크롤 가장 아래로

            // 키보드 Open/Close 체크
            ctlContainer.viewTreeObserver.addOnGlobalLayoutListener {
                val rect = Rect()
                ctlContainer.getWindowVisibleDisplayFrame(rect)

                val rootViewHeight = binding.ctlContainer.rootView.height
                val heightDiff = rootViewHeight - rect.height()
                isKeyboardOpen = heightDiff > rootViewHeight * 0.25 // true == 키보드 올라감
            }

            rvChat.apply {
                addOnLayoutChangeListener(onLayoutChangeListener)
                viewTreeObserver.addOnScrollChangedListener {
                    if (isScrollable() && !isKeyboardOpen) { // 스크롤이 가능하면서 키보드가 닫힌 상태일 떄만
                        setStackFromEnd()
                        removeOnLayoutChangeListener(onLayoutChangeListener)
                    }
                }
            }
        }
    }

    companion object {
        fun start(context: Context, chatRoom: ChatRoom) {
            val intent = Intent(context, ChatRoomActivity::class.java)
            intent.putExtra(GO_CHAT_ROOM_KEY, chatRoom)
            context.startActivity(intent)
        }

        private const val GO_CHAT_ROOM_KEY = "com.depromeet.baton.presentation.ui.chatting"
    }
}