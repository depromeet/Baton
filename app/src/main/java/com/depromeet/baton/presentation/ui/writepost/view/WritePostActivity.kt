package com.depromeet.baton.presentation.ui.writepost.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityWritePostBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.ui.writepost.viewmodel.WritePostViewModel
import com.depromeet.baton.presentation.util.shortToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WritePostActivity : BaseActivity<ActivityWritePostBinding>(R.layout.activity_write_post) {
    private val writePostViewModel: WritePostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.writePost = this
        binding.writePostViewModel = writePostViewModel

        moveToNextLevel(PlaceRegisterFragment())
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
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    WritePostViewModel.GO_TO_MEMBERSHIP_INFO -> moveToNextLevel(MembershipformationFragment())
                    WritePostViewModel.GO_TO_TRANSACTION_METHOD -> moveToNextLevel(TransactionMethodRegisterFragment())
                    WritePostViewModel.GO_TO_DESCRIPTION -> moveToNextLevel(DescriptionFragment())
                    WritePostViewModel.GO_TO_DONE -> {
                        shortToast("완작 성료")  //TODO INTENT
                        finish ()
                    }
                }
            }
        }
        writePostViewModel.currentLevel.observe(this) { currentLevel ->
            if (currentLevel == 0) finish()
            if (currentLevel == 4) binding.btnWritePostNext.text = "완료"
        }
    }

    private fun moveToNextLevel(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fcv_write_post, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun backToPreviousLevel() {
        writePostViewModel.setNextLevel(false)
        supportFragmentManager.popBackStack()
    }

    private fun setCloseWritePostOnClickListener() {
        binding.bdsBackwardAppbarWritePost.setOnBackwardClick { finish() }
    }

    override fun onBackPressed() {
        backToPreviousLevel()
    }
}

