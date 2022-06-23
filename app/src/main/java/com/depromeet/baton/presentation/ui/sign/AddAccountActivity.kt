package com.depromeet.baton.presentation.ui.sign

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityAddAccountBinding
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.bottom.BottomMenuItem
import com.depromeet.baton.presentation.bottom.BottomSheetFragment
import com.depromeet.baton.presentation.ui.sign.AddAccountViewModel.ViewEvent
import com.depromeet.baton.presentation.util.RegexConstant
import com.depromeet.bds.component.BdsToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class SignAccount(
    val name: String,
    val bank: String,
    val account: String,
) : Parcelable

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
        val account = intent.getParcelableExtra<SignAccount>(ACCOUNT)
        viewModel.setAccount(account)
        binding.etAccount.setText(account?.account)
        binding.etName.setText(account?.name)
    }

    private fun handleViewEvents(viewEvents: List<ViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                is ViewEvent.AddAccountDone -> {
                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(RESULT_ARGS, viewEvent.account)
                    })

                    // do something
                    this.BdsToast("계좌가 등록됐어요.").show()
                    finish()
                }
                is ViewEvent.OpenBankSelection -> {
                    showBankBottom()
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }

    private fun showBankBottom() {
        val list = resources.getStringArray(R.array.bank_items).map {
            BottomMenuItem(it, it == viewModel.uiState.value.bank)
        }
        val bottom =
            BottomSheetFragment.newInstance(
                "은행선택",
                list,
                BottomSheetFragment.CHECK_ITEM_VIEW,
                object :
                    BottomSheetFragment.Companion.OnItemClick {
                    override fun onSelectedItem(selected: BottomMenuItem, pos: Int) {
                        viewModel.handleBankSelected(selected.listItem!!)
                    }
                })
        bottom.show(supportFragmentManager, null)
    }

    companion object {
        private const val ACCOUNT = "account"
        const val RESULT_ARGS = "result_args"

        fun intent(context: Context, signAccount: SignAccount?): Intent {
            return Intent(context, AddAccountActivity::class.java).apply {
                putExtra(ACCOUNT, signAccount)
            }
        }
    }
}

//TODO: validation 적용하기
data class AddAccountUiState(
    val name: String,
    val bank: String,
    val account: String,
    val onNameChanged: (Editable?) -> Unit,
    val onBankSelected: (String) -> Unit,
    val onAccountChanged: (Editable?) -> Unit,
    val onBankSelectionClick: () -> Unit,
    val onClickSubmit: () -> Unit
) {
    private val isNameValid = name.isNotBlank() && RegexConstant.ONLY_COMPLETE_HANGLE.matches(name)

    //BEAN: 일단 무조건 통과로
    private val isAccountValid = true

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
            onClickSubmit = ::submit
        )
    }

    fun setAccount(account: SignAccount?) {
        _uiState.update {
            it.copy(
                name = account?.name.orEmpty(),
                bank = account?.bank.orEmpty(),
                account = account?.account.orEmpty(),
            )
        }
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

    private fun submit() {
        makeAccount()?.let { addViewEvent(ViewEvent.AddAccountDone(it)) }
    }

    private fun makeAccount(): SignAccount? {
        val state = _uiState.value

        val name = state.name
        val bank = state.bank
        val account = state.account

        if (name.isBlank() || bank.isBlank() || account.isBlank()) return null
        return SignAccount(name, bank, account)
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    sealed interface ViewEvent {
        data class AddAccountDone(val account: SignAccount) : ViewEvent
        data class OpenBankSelection(val selectedBank: String) : ViewEvent
    }
}
