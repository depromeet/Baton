package com.depromeet.baton.presentation.ui.writepost.view

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityWritePostBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class WritePostActivity : BaseActivity<ActivityWritePostBinding>(R.layout.activity_write_post) {
    private val writePostViewModel: WritePostViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.writePost = this
        binding.writePostViewModel = writePostViewModel
        setObserve()
        setInitOnClickListener()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObserve() {
        writePostViewModel.writePostPositionViewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleViewEvents)
            .launchIn(lifecycleScope)
    }

    private fun setInitOnClickListener() {
        binding.btnWritePostBack.setOnClickListener {
            backToPreviousLevel()
        }
        binding.btnWritePostNext.setOnClickListener {
            writePostViewModel.setNextLevel()
        }
        binding.bdsBackwardAppbarWritePost.setOnBackwardClick {
            //todo 임시저장
            //  this.BdsToast("작성하던 글이 임시저장 됐어요.", binding.btnWritePostBack.top).show()
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleViewEvents(viewEvents: List<WritePostViewModel.WritePostPositionViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                WritePostViewModel.WritePostPositionViewEvent.GoMembershipInfo -> {
                    moveToNextLevel(MembershipInformationFragment())
                }
                WritePostViewModel.WritePostPositionViewEvent.GoTransactionMethod -> {
                    moveToNextLevel(TransactionMethodRegisterFragment())
                }
                WritePostViewModel.WritePostPositionViewEvent.GoDescription -> {
                    moveToNextLevel(DescriptionFragment())
                }
                WritePostViewModel.WritePostPositionViewEvent.GoDone -> {
                    writePostViewModel.postTicket()
                }
            }
            writePostViewModel.writePositionConsumeViewEvent(viewEvent)
        }

        writePostViewModel.currentLevel.observe(this) { currentLevel ->
            when (currentLevel) {
                0 -> finish()
                in 4..5 -> binding.btnWritePostNext.setText("완료")
                else -> binding.btnWritePostNext.setText("다음")
            }
        }

        writePostViewModel.postSuccess.observe(this) {
            //  this.BdsToast("판매글 등록이 완료됐어요.", binding.btnWritePostBack.top).show()
            startActivity(TicketDetailActivity.start(this@WritePostActivity, writePostViewModel.postId.value!!))
            finish()
        }
    }

    private fun moveToNextLevel(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fcv_write_post, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun backToPreviousLevel() {
        writePostViewModel.setNextLevel(false)
        supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        backToPreviousLevel()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, WritePostActivity::class.java)
            context.startActivity(intent)
        }
    }
}

