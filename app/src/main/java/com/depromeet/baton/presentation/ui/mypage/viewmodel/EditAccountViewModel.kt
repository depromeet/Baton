package com.depromeet.baton.presentation.ui.mypage.viewmodel

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.R
import com.depromeet.baton.domain.repository.AccountRepository
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.util.RegexConstant
import com.depromeet.baton.remote.user.UserAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class EditAccountUiState(
    val name: String,
    val bank: String,
    val account: String,
    val onNameChanged: (Editable?) -> Unit,
    val onBankSelected: (String) -> Unit,
    val onAccountChanged: (Editable?) -> Unit,
    val onBankSelectionClick: () -> Unit,
    val removeAccount : () ->Unit,
    val submit : ()-> Unit
) {
    private val isNameValid = name.isNotEmpty() && RegexConstant.ONLY_COMPLETE_HANGLE.matches(name)

    private val isAccountValid = account.length >=11 && RegexConstant.ONLY_NUMBERS.matches(account)

    val nameErrorReason = if (isNameValid) null else "올바른 예금주명을 입력해주세요."
    val accountErrorReason = if (isAccountValid) null else "올바른 계좌번호를 입력해주세요."

    val isEnabled =   isNameValid && isAccountValid && bank.isNotBlank()
}


@HiltViewModel
class EditAccountViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
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
            onNameChanged = ::handleNameChanged,
            onBankSelected = ::handleBankSelected,
            onAccountChanged = ::handleAccountChanged,
            onBankSelectionClick = ::handleBankSelectionClick,
            removeAccount = ::removeAccount,
            submit = ::submit
        )
    }

    private fun editAccount(){
        viewModelScope.launch {
            val userId = authRepository.authInfo!!.userId
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

    fun submit(){
        if(uiState.value.isEnabled) editAccount()
    }

    private fun removeAccount() {
        viewModelScope.launch {
            val userId = authRepository.authInfo!!.userId
            runCatching {
                uiState?.let {
                    accountRepository.deleteAccount(userId)
                }
            }.onSuccess {
                when(it){
                    is NetworkResult.Success ->{ addViewEvent(ViewEvent.RemoveAccountDone)}
                    is NetworkResult.Error ->{
                        Timber.e(it.message)
                        addViewEvent(ViewEvent.EditAccountFailure("계좌삭제 실패"))
                    }
                }
            }
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

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    sealed interface ViewEvent {
        object EditAccountDone : ViewEvent
        object RemoveAccountDone : ViewEvent
        data class EditAccountFailure(val msg: String) : ViewEvent
        data class OpenBankSelection(val selectedBank: String) : ViewEvent
    }


}
