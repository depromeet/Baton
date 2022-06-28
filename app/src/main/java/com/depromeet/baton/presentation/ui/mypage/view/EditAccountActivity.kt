package com.depromeet.baton.presentation.ui.mypage.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityEditAccountBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.bottom.BottomSheetFragment.Companion.CHECK_ITEM_VIEW
import com.depromeet.baton.presentation.ui.mypage.viewmodel.EditAccountViewModel
import com.depromeet.baton.remote.user.UserAccount
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class EditAccountActivity :BaseActivity<ActivityEditAccountBinding>(R.layout.activity_edit_account){
    private val viewModel by viewModels<EditAccountViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
        setListener()
    }

    private fun setListener(){
        binding.appbar.setOnBackwardClick{onBackPressed()}
        binding.appbar.setOnButton{
            viewModel.submit()
        }
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

    fun handleViewEvents(viewEvents: List<EditAccountViewModel.ViewEvent>){
            viewEvents.firstOrNull()?.let { viewEvent ->
                when (viewEvent) {
                    is EditAccountViewModel.ViewEvent.EditAccountDone -> {
                        this.BdsToast("계좌 수정이 완료됐어요.",binding.root.bottom).show()
                        finish()
                    }
                    is EditAccountViewModel.ViewEvent.RemoveAccountDone ->{
                       this.BdsToast("계좌가 삭제되었어요.",binding.root.bottom).show()
                        finish()
                    }
                    is EditAccountViewModel.ViewEvent.EditAccountFailure ->{
                        this.BdsToast(viewEvent.msg).show()
                    }
                    is EditAccountViewModel.ViewEvent.OpenBankSelection -> {
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
        val bottom =BottomSheetFragment.newInstance("은행선택",list,CHECK_ITEM_VIEW ,object:
            BottomSheetFragment.Companion.OnItemClick {
            override fun onSelectedItem(selected: BottomMenuItem, pos: Int) {
              viewModel.handleBankSelected(selected.listItem!!)
            }
        })
        bottom.show(supportFragmentManager,null)
    }

    companion object{
        fun start(context: Context, account : UserAccount){
            val intent = Intent(context, EditAccountActivity::class.java)
            val bundle = bundleOf("data" to account)
            intent.putExtra("account",bundle)
            context.startActivity(intent)
        }
    }
}
