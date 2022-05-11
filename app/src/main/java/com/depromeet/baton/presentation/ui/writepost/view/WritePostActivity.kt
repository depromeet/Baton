package com.depromeet.baton.presentation.ui.writepost.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityWritePostBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WritePostActivity : BaseActivity<ActivityWritePostBinding>(R.layout.activity_write_post) {
    private val writePostViewModel: WritePostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.writePost = this
        binding.writePostViewModel = writePostViewModel

        moveToNextLevel(PlaceRegisterFragment())
        setNextLevel()
        setCloseWritePostOnClickListener()
        setBackBtnOnClickListener()
        setNextLevelBtnOnClickListener()
    }

    //TODO 뒤로가기
    private fun setBackBtnOnClickListener() {
        binding.bdsButtonBack.setOnClickListener {
            writePostViewModel.setNextLevel(false)
            supportFragmentManager.popBackStack()
        }
        writePostViewModel.currentLevel.observe(this) {
            if (it == 0) finish()
        }
    }

    //TODO 앞으로가기
    private fun setNextLevelBtnOnClickListener() {
        binding.bdsButttonPlaceRegisterNext.setOnClickListener {
            writePostViewModel.setNextLevel()
        }
    }

    private fun setNextLevel() {
        writePostViewModel.viewEvent.observe(this) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    WritePostViewModel.GO_TO_TICKET_INFO -> moveToNextLevel(TicketInformationFragment())
                    WritePostViewModel.GO_TO_TRANSACTION_METHOD -> moveToNextLevel(TransactionMethodRegisterFragment())
                    WritePostViewModel.GO_TO_DESCRIPTION -> moveToNextLevel(DescriptionFragment())
                }
            }
        }
    }

    private fun moveToNextLevel(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fcv_write_post, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setCloseWritePostOnClickListener() {
        binding.bdsBackwardAppbarWritePost.setOnBackwardClick { finish() }
    }
}

