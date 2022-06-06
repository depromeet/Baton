package com.depromeet.baton.presentation.ui.writepost.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityWritePostBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.ui.address.view.AddressActivity
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WritePostActivity : BaseActivity<ActivityWritePostBinding>(R.layout.activity_write_post) {
    private val writePostViewModel: WritePostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.writePost = this
        binding.writePostViewModel = writePostViewModel
        setObserve()
        setCloseWritePostOnClickListener()
        setBackBtnOnClickListener()
        setNextLevelBtnOnClickListener()
    }

    private fun setBackBtnOnClickListener() {
        binding.btnWritePostBack.setOnClickListener {
            backToPreviousLevel()
        }
    }

    private fun setNextLevelBtnOnClickListener() {
        binding.btnWritePostNext.setOnClickListener {
            writePostViewModel.setNextLevel()
        }
    }

    private fun setObserve() {
        writePostViewModel.viewEvent.observe(this) {
            it.getContentIfNotHandled().let { event ->
                when (event) {
                    WritePostViewModel.GO_TO_MEMBERSHIP_INFO -> moveToNextLevel(MembershipInformationFragment())
                    WritePostViewModel.GO_TO_TRANSACTION_METHOD -> moveToNextLevel(TransactionMethodRegisterFragment())
                    WritePostViewModel.GO_TO_DESCRIPTION -> moveToNextLevel(DescriptionFragment())
                    WritePostViewModel.GO_TO_DONE -> {
                        this.BdsToast("판매글 작성이 완료됐어요", binding.btnWritePostBack.top).show()
                        startActivity(Intent(this, TicketDetailActivity::class.java))
                        finish()
                    }
                }
            }
        }
        writePostViewModel.currentLevel.observe(this) { currentLevel ->
            if (currentLevel == 0) finish()
            //      if (currentLevel == 4) binding.btnWritePostNext
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

    private fun setCloseWritePostOnClickListener() {

        binding.bdsBackwardAppbarWritePost.setOnBackwardClick {
            //todo 임시저장
            //   this.BdsToast("작성하던 글이 임시저장 됐어요.", binding.btnWritePostBack.top).show()
            finish()
        }
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

