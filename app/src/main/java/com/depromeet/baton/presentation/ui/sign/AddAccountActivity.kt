package com.depromeet.baton.presentation.ui.sign

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityAddAccountBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.sign.AddAccountViewModel.ViewEvent
import com.depromeet.baton.presentation.util.RegexConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AddAccountActivity : BaseActivity<ActivityAddAccountBinding>(R.layout.activity_add_account) {

    private val viewModel: AddAccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.appbar.setOnBackwardClick { onBackPressed() }


        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(lifecycleScope)

        viewModel.viewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleViewEvents)
            .launchIn(lifecycleScope)
    }

    private fun handleViewEvents(viewEvents: List<ViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                ViewEvent.AddAccountDone -> {
                    // do something
                }
                is ViewEvent.OpenBankSelection -> {
                    //BEAN: 은행 선택 바텀 시트 추가 필요
//                    BottomSheetFragment(
//                        "은행 선택",
//                        AddAccountViewModel.supportedBanks.map {
//                            BottomMenuItem(it, false)
//                        }
//                    )
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddAccountActivity::class.java)
            context.startActivity(intent)
        }
    }
}

data class AddAccountUiState(
    val name: String,
    val bank: String,
    val account: String,
    val onNameChanged: (Editable?) -> Unit,
    val onBankSelected: (String) -> Unit,
    val onAccountChanged: (Editable?) -> Unit,
    val onBankSelectionClick: () -> Unit,
) {
    private val isNameValid = name.isNotBlank() && RegexConstant.ONLY_COMPLETE_HANGLE.matches(name)

    //BEAN: 일단 계좌 번호 길이 13으루
    private val isAccountValid = account.length == 13

    val nameErrorReason = if (isNameValid) null else "올바른 예금주명을 입력해주세요."
    val accountErrorReason = if (isAccountValid) null else "올바른 계좌번호를 입력해주세요."

    val isEnabled = isNameValid && isAccountValid && bank.isNotBlank()
}

@HiltViewModel
class AddAccountViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState: MutableStateFlow<AddAccountUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun createState(): AddAccountUiState {
        return AddAccountUiState(
            name = "",
            bank = "",
            account = "",
            onNameChanged = ::handleNameChanged,
            onBankSelected = ::handleBankSelected,
            onAccountChanged = ::handleAccountChanged,
            onBankSelectionClick = ::handleBankSelectionClick,
        )
    }

    private fun handleNameChanged(editable: Editable?) {
        _uiState.update { it.copy(name = editable.toString()) }
    }

    fun handleBankSelected(bank: String) {
        _uiState.update { it.copy(bank = bank) }
    }

    private fun handleAccountChanged(editable: Editable?) {
        _uiState.update { it.copy(account = editable.toString()) }
    }

    private fun handleBankSelectionClick() {
        val currentBank = uiState.value.bank
        addViewEvent(ViewEvent.OpenBankSelection(currentBank))
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    sealed interface ViewEvent {
        object AddAccountDone : ViewEvent
        data class OpenBankSelection(val selectedBank: String) : ViewEvent
    }

    companion object {
        val supportedBanks = listOf(
            "NH농협은행",
            "KB국민은행",
            "카카오뱅크",
            "신한은행",
            "우리은행",
            // and more ...
        )
    }
}
