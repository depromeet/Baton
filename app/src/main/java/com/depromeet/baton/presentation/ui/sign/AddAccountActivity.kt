package com.depromeet.baton.presentation.ui.sign

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber
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

data class AddAccountUiState(
    val name: Validation,
    val bank: String,
    val account: Validation,
    val onNameChanged: (Editable?) -> Unit,
    val onBankSelected: (String) -> Unit,
    val onAccountChanged: (Editable?) -> Unit,
    val onBankSelectionClick: () -> Unit,
    val onClickSubmit: () -> Unit
) {
    private fun Validation.isEnabled(): Boolean {
        return value.isNotBlank() && errorReason == null && !isValidating
    }

    val isEnabled = name.isEnabled() && bank.isNotBlank()
}

@HiltViewModel
class AddAccountViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState: MutableStateFlow<AddAccountUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun createState(): AddAccountUiState {
        return AddAccountUiState(
            name = Validation(),
            bank = "",
            account = Validation(),
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
                name = it.name.copy(value = account?.name.orEmpty()),
                bank = account?.bank.orEmpty(),
                account = it.account.copy(value = account?.account.orEmpty()),
            )
        }
    }

    private var job1: Job? = null
    private fun handleNameChanged(editable: Editable?) {
        job1?.cancel()

        val value = editable.toString()
        _uiState.update {
            it.copy(
                name = it.name.copy(
                    value = value,
                    isValidating = false,
                    errorReason = null
                )
            )
        }

        if (RegexConstant.ONLY_COMPLETE_HANGLE.matches(value)) return

        _uiState.update { it.copy(name = it.name.copy(isValidating = true)) }

        job1 = viewModelScope.launch {
            delay(FIELD_VALIDATION_MILLIS)
            _uiState.update {
                it.copy(
                    name = it.name.copy(
                        errorReason = "올바른 예금주명을 입력해주세요.",
                        isValidating = false
                    )
                )
            }
        }
    }

    fun handleBankSelected(bank: String) {
        _uiState.update { it.copy(bank = bank) }
    }

    private fun handleAccountChanged(editable: Editable?) {
        //FIXME: 아직 account에 validation 동작이 없다.
        val value = editable.toString()
        _uiState.update {
            it.copy(
                account = it.account.copy(
                    value = value,
                    isValidating = false,
                    errorReason = null
                )
            )
        }
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

        val name = state.name.value
        val bank = state.bank
        val account = state.account.value

        Timber.d("beanbean makeAccount > $name $bank $account")
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

    companion object {
        private const val FIELD_VALIDATION_MILLIS = 1000L
    }
}
