package com.depromeet.baton.presentation.ui.mypage.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityEditAccountBinding
import com.depromeet.baton.domain.repository.AccountRepository
import com.depromeet.baton.domain.repository.UserinfoRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.bottom.BottomSheetFragment.Companion.CHECK_ITEM_VIEW
import com.depromeet.baton.presentation.ui.mypage.viewmodel.EditAccountViewModel
import com.depromeet.baton.remote.user.UserAccount
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

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
            viewModel.checkEditOption()
        }
    }

    private fun setObserver(){
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState ->
                binding.uiState = uiState
                binding.appbar.setButtonText(uiState.isCheckedStr)
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
                        this.BdsToast("계좌 수정이 완료됐어요.").show()
                    }
                    is EditAccountViewModel.ViewEvent.EditAccountFailure ->{
                        this.BdsToast(viewEvent.msg).show()
                    }
                    is EditAccountViewModel.ViewEvent.OpenBankSelection -> {
                        //BEAN: 은행 선택 바텀 시트 추가 필요
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
