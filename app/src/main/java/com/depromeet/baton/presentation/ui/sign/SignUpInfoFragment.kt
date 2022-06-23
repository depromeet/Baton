package com.depromeet.baton.presentation.ui.sign

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSignUpInfoBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.sign.SignUpInfoViewModel.ViewEvent
import com.depromeet.baton.presentation.util.RegexConstant
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignUpInfoFragment : BaseFragment<FragmentSignUpInfoBinding>(R.layout.fragment_sign_up_info) {
    private val viewModel: SignUpInfoViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpViewModel.setCurrentStep(2)

        viewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(viewLifecycleScope)

        viewModel.viewEvents
            .flowWithLifecycle(viewLifecycle)
            .onEach(::handleViewEvents)
            .launchIn(viewLifecycleScope)
    }

    private fun handleViewEvents(viewEvents: List<ViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                ViewEvent.ToAddAccount -> {
                    AddAccountActivity.start(requireContext())
                }
                ViewEvent.ToAddressSetting -> {
                    signUpViewModel.remember(viewModel)
                    SignUpInfoFragmentDirections.actionSignUpInfoFragmentToSignUpAddressFragment()
                        .also { navController.navigate(it) }
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }
}

data class Validation(
    val value: String = "",
    val errorReason: String? = null,
    val isValidating: Boolean = false
)

data class SignUpInfoUiState(
    val name: Validation,
    val nickName: Validation,
    val phoneNumber: Validation,
    val onNameChanged: (Editable?) -> Unit,
    val onNickNameChanged: (Editable?) -> Unit,
    val onPhoneNumberChanged: (Editable?) -> Unit,
    val onAddAccountClick: () -> Unit,
    val onSubmit: () -> Unit,
) {
    private fun Validation.isEnabled(): Boolean {
        return value.isNotBlank() && errorReason == null && !isValidating
    }

    val isEnabled = name.isEnabled() && nickName.isEnabled() && phoneNumber.isEnabled()
}

@HiltViewModel
class SignUpInfoViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState: MutableStateFlow<SignUpInfoUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun createState(): SignUpInfoUiState {
        return SignUpInfoUiState(
            name = Validation(),
            nickName = Validation(),
            phoneNumber = Validation(),
            onNameChanged = ::handleNameChanged,
            onNickNameChanged = ::handleNickNameChanged,
            onPhoneNumberChanged = ::handlePhoneNumberChanged,
            onAddAccountClick = ::handleAddAccountClick,
            onSubmit = ::handleSubmit,
        )
    }

    private var nameValidationJob: Job? = null
    private fun handleNameChanged(text: Editable?) {
        nameValidationJob?.cancel()

        val value = text.toString()
        _uiState.update { it.copy(name = it.name.copy(value = value, errorReason = null, isValidating = false)) }

        if (RegexConstant.ONLY_COMPLETE_HANGLE.matches(value)) return

        _uiState.update { it.copy(name = it.name.copy(isValidating = true)) }

        nameValidationJob = viewModelScope.launch {
            delay(FIELD_VALIDATION_MILLIS)
            _uiState.update { it.copy(name = it.name.copy(errorReason = "올바른 이름을 입력해주세요.", isValidating = false)) }
        }
    }

    private var nickNameValidationJob: Job? = null
    private fun handleNickNameChanged(text: Editable?) {
        nickNameValidationJob?.cancel()

        val value = text.toString()
        _uiState.update { it.copy(nickName = it.nickName.copy(value = value, errorReason = null, isValidating = false)) }

        if (RegexConstant.NICKNAME_REGEX.matches(value)) return

        _uiState.update { it.copy(nickName = it.nickName.copy(isValidating = true)) }

        nickNameValidationJob = viewModelScope.launch {
            delay(FIELD_VALIDATION_MILLIS)
            _uiState.update { it.copy(nickName = it.nickName.copy(errorReason = "올바른 닉네임을 입력해주세요.", isValidating = false)) }
        }
    }

    private var phoneNumberValidationJob: Job? = null
    private fun handlePhoneNumberChanged(text: Editable?) {
        phoneNumberValidationJob?.cancel()

        val value = text.toString()
        _uiState.update { it.copy(phoneNumber = it.phoneNumber.copy(value = value, errorReason = null, isValidating = false)) }

        if (RegexConstant.ONLY_NUMBERS.matches(value) && value.length == 11) return

        _uiState.update { it.copy(phoneNumber = it.phoneNumber.copy(isValidating = true)) }

        phoneNumberValidationJob = viewModelScope.launch {
            delay(FIELD_VALIDATION_MILLIS)
            _uiState.update { it.copy(phoneNumber = it.phoneNumber.copy(errorReason = "올바른 휴대폰 번호를 입력해주세요.", isValidating = false)) }
        }
    }

    private fun handleAddAccountClick() {
        addViewEvent(ViewEvent.ToAddAccount)
    }

    private fun handleSubmit() {
        addViewEvent(ViewEvent.ToAddressSetting)
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    sealed interface ViewEvent {
        object ToAddAccount : ViewEvent
        object ToAddressSetting : ViewEvent
    }

    companion object {
        private const val FIELD_VALIDATION_MILLIS = 1000L
    }
}
