package com.depromeet.baton.presentation.ui.sign

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
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
import kotlinx.coroutines.flow.*
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
                    navController.navigate(R.id.action_signUpInfoFragment_to_signUpAddressFragment)
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }
}

data class SignUpInfoUiState(
    val name: String,
    val nickName: String,
    val phoneNumber: String,
    val onNameChanged: (Editable?) -> Unit,
    val onNickNameChanged: (Editable?) -> Unit,
    val onPhoneNumberChanged: (Editable?) -> Unit,
    val onAddAccountClick: () -> Unit,
    val onSubmit: () -> Unit,
) {
    val nameErrorReason =
        if (RegexConstant.ONLY_COMPLETE_HANGLE.matches(name)) null
        else "올바른 이름을 입력해주세요."
    val nickNameErrorReason =
        if (RegexConstant.ONLY_COMPLETE_HANGLE_SPACE.matches(nickName)) null
        else "올바른 닉네임을 입력해주세요."
    val phoneNumberErrorReason =
        if (RegexConstant.ONLY_NUMBERS.matches(phoneNumber) && phoneNumber.length == 11) null
        else "올바른 휴대폰 번호를 입력해주세요."

    val isEnabled = name.isNotBlank() &&
            nameErrorReason == null &&
            phoneNumber.isNotBlank() &&
            phoneNumberErrorReason == null
}

@HiltViewModel
class SignUpInfoViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState: MutableStateFlow<SignUpInfoUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun createState(): SignUpInfoUiState {
        return SignUpInfoUiState(
            name = "",
            nickName = "",
            phoneNumber = "",
            onNameChanged = ::handleNameChanged,
            onNickNameChanged = ::handleNickNameChanged,
            onPhoneNumberChanged = ::handlePhoneNumberChanged,
            onAddAccountClick = ::handleAddAccountClick,
            onSubmit = ::handleSubmit,
        )
    }

    private fun handleNameChanged(text: Editable?) {
        _uiState.update { it.copy(name = text.toString()) }
    }

    private fun handleNickNameChanged(text: Editable?) {
        _uiState.update { it.copy(nickName = text.toString()) }
    }

    private fun handlePhoneNumberChanged(text: Editable?) {
        _uiState.update { it.copy(phoneNumber = text.toString()) }
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
}
