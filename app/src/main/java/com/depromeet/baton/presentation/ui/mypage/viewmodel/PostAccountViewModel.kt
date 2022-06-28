package com.depromeet.baton.presentation.ui.mypage.viewmodel

import android.os.Bundle
import android.text.Editable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
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


@HiltViewModel
class PostAccountViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<PostAccountUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun createState():  PostAccountUiState {
        return PostAccountUiState(
            name = "",
            bank = "",
            account = "",
            onNameChanged = ::handleNameChanged,
            onBankSelected = ::handleBankSelected,
            onAccountChanged = ::handleAccountChanged,
            onBankSelectionClick = ::handleBankSelectionClick,
            submit = ::checkOption
        )
    }

    private fun postAccount(){
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
                        addViewEvent(ViewEvent.EditAccountFailure("계좌 등록에 실패했습니다."))
                    }
                }
            }
        }
    }

    private fun checkOption(){
        if(uiState.value.isEnabled) postAccount()
        else  {
            addViewEvent(ViewEvent.EditAccountFailure("입력 정보를 확인해주세요."))
            return
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
        data class EditAccountFailure(val msg: String) : ViewEvent
        data class OpenBankSelection(val selectedBank: String) : ViewEvent
    }


}
data class PostAccountUiState(
    val name: String,
    val bank: String,
    val account: String,
    val onNameChanged: (Editable?) -> Unit,
    val onBankSelected: (String) -> Unit,
    val onAccountChanged: (Editable?) -> Unit,
    val onBankSelectionClick: () -> Unit,
    val submit : () -> Unit,
) {
    private val isNameValid = name.isNotBlank() && RegexConstant.ONLY_COMPLETE_HANGLE.matches(name)

    private val isAccountValid = account.length >=11&& RegexConstant.ONLY_NUMBERS.matches(account)

    val nameErrorReason : String?=  if (isNameValid || name.isBlank()) null else "올바른 예금주명을 입력해주세요."
    val accountErrorReason = if (isAccountValid || account.isBlank()) null else "올바른 계좌번호를 입력해주세요."

    val isEnabled =  isNameValid && isAccountValid && bank.isNotBlank()

}
