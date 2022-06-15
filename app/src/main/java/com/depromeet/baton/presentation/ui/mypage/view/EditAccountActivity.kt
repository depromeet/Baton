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
import com.depromeet.baton.presentation.ui.sign.AddAccountUiState
import com.depromeet.baton.presentation.ui.sign.AddAccountViewModel
import com.depromeet.baton.presentation.util.RegexConstant
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

    companion object{
        fun start(context: Context, account : UserAccount){
            val intent = Intent(context, EditAccountActivity::class.java)
            val bundle = bundleOf("data" to account)
            intent.putExtra("account",bundle)
            context.startActivity(intent)
        }
    }
}
data class EditAccountUiState(
    val name: String,
    val bank: String,
    val account: String,
    val checkEdit : Boolean,
    val onNameChanged: (Editable?) -> Unit,
    val onBankSelected: (String) -> Unit,
    val onAccountChanged: (Editable?) -> Unit,
    val onBankSelectionClick: () -> Unit,
) {
    private val isNameValid = name.isNotBlank() && RegexConstant.ONLY_COMPLETE_HANGLE.matches(name)

    //BEAN: 일단 계좌 번호 길이 13으루
    private val isAccountValid = account.length == 13&&RegexConstant.ONLY_NUMBERS.matches(account)

    val nameErrorReason = if (isNameValid) null else "올바른 예금주명을 입력해주세요."
    val accountErrorReason = if (isAccountValid) null else "올바른 계좌번호를 입력해주세요."

    val isEnabled =  checkEdit && isNameValid && isAccountValid && bank.isNotBlank()
    val isCheckedStr = if(checkEdit)"완료" else "수정"
}


@HiltViewModel
class EditAccountViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<EditAccountUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun createState():EditAccountUiState {
       val account= savedStateHandle.get<Bundle>("account") as Bundle
       val info =  account.get("data") as UserAccount
     
        return EditAccountUiState(
            name = info.holder,
            bank = info.bank,
            account = info.number,
            checkEdit = false,
            onNameChanged = ::handleNameChanged,
            onBankSelected = ::handleBankSelected,
            onAccountChanged = ::handleAccountChanged,
            onBankSelectionClick = ::handleBankSelectionClick,
        )
    }

    fun editAccount(){
        viewModelScope.launch {
            val userId =2
            runCatching {
                uiState?.let {
                    accountRepository.updateAccount(userId,it.value.name, it.value.bank,it.value.account)
                }
            }.onSuccess {
                when(it){
                    is NetworkResult.Success ->{ addViewEvent(ViewEvent.EditAccountDone)}
                    is NetworkResult.Error ->{
                        Timber.e(it.message)
                        addViewEvent(ViewEvent.EditAccountFailure("계좌변경 실패"))
                    }
                }
            }
        }
    }

    fun checkEditOption(){
        if(uiState.value.isEnabled) editAccount()
        else if(uiState.value.checkEdit)  addViewEvent(ViewEvent.EditAccountFailure("입력 정보를 확인해주세요."))
        _uiState.update { it.copy(checkEdit = !it.checkEdit) }
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
        object EditAccountDone : ViewEvent
        data class EditAccountFailure(val msg: String) : ViewEvent
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
