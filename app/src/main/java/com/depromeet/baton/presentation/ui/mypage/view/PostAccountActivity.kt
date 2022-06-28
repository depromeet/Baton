package com.depromeet.baton.presentation.ui.mypage.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityEditAccountBinding
import com.depromeet.baton.databinding.ActivityPostAccountBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.ui.mypage.viewmodel.PostAccountViewModel
import com.depromeet.baton.presentation.util.RegexConstant
import com.depromeet.baton.remote.user.UserAccount
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class PostAccountActivity : BaseActivity<ActivityPostAccountBinding>(R.layout.activity_post_account){
    private val viewModel by viewModels<PostAccountViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
        setListener()
    }

    private fun setListener(){
        binding.appbar.setOnBackwardClick{onBackPressed()}
    }

    private fun setObserver(){
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState ->
                binding.uiState = uiState
            }
            .launchIn(lifecycleScope)

        viewModel.viewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleViewEvents)
            .launchIn(lifecycleScope)
    }

    fun handleViewEvents(viewEvents: List<PostAccountViewModel.ViewEvent>){
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                is PostAccountViewModel.ViewEvent.EditAccountDone -> {
                    this.BdsToast("계좌가 등록 됐어요.",binding.root.bottom).show()
                    finish()
                }
                is PostAccountViewModel.ViewEvent.EditAccountFailure ->{
                    this.BdsToast(viewEvent.msg).show()
                }
                is  PostAccountViewModel.ViewEvent.OpenBankSelection -> {
                    showBankBottom()
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }

    private fun showBankBottom(){
        val list =  resources.getStringArray(R.array.bank_items).map {
            BottomMenuItem(it, it==viewModel.uiState.value.bank)
        }
        val bottom =
            BottomSheetFragment.newInstance("은행선택",list, BottomSheetFragment.CHECK_ITEM_VIEW,object:
            BottomSheetFragment.Companion.OnItemClick {
            override fun onSelectedItem(selected: BottomMenuItem, pos: Int) {
                viewModel.handleBankSelected(selected.listItem!!)
            }
        })
        bottom.show(supportFragmentManager,null)
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context, PostAccountActivity::class.java)
            context.startActivity(intent)
        }
    }
}

