package com.depromeet.baton.presentation.ui.sign

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSignUpTermBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.sign.SignUpTermViewModel.ViewEvent
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@AndroidEntryPoint
class SignUpTermFragment : BaseFragment<FragmentSignUpTermBinding>(R.layout.fragment_sign_up_term) {
    private val viewModel: SignUpTermViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                ViewEvent.ToServiceDetail -> {
                    ServiceTermDetailActivity.start(requireContext())
                }
                ViewEvent.ToPrivacyDetail -> {
                    PrivacyTermDetailActivity.start(requireContext())
                }
                ViewEvent.ToInfo -> {
                    navController.navigate(R.id.action_signUpTermFragment_to_signUpInfoFragment)
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }
}


data class SignUpTermUiState(
    val isAgreeAge: Boolean,
    val isAgreeService: Boolean,
    val isAgreePrivacy: Boolean,
    val onClickAgreeAll: () -> Unit,
    val onClickAgreeAge: () -> Unit,
    val onClickAgreeService: () -> Unit,
    val onClickAgreePrivacy: () -> Unit,
    val onClickServiceDetail: () -> Unit,
    val onClickPrivacyDetail: () -> Unit,
    val onSubmit: () -> Unit,
) {
    val isAgreeAll = isAgreeAge && isAgreeService && isAgreePrivacy
    val isEnabled = isAgreeAge && isAgreeService && isAgreePrivacy
}

@HiltViewModel
class SignUpTermViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState: MutableStateFlow<SignUpTermUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun createState(): SignUpTermUiState {
        return SignUpTermUiState(
            isAgreeAge = false,
            isAgreeService = false,
            isAgreePrivacy = false,
            onClickAgreeAll = ::handleClickAgreeAll,
            onClickAgreeAge = ::handleClickAgreeAge,
            onClickAgreeService = ::handleClickService,
            onClickAgreePrivacy = ::handleClickPrivacy,
            onClickServiceDetail = ::handleClickServiceDetail,
            onClickPrivacyDetail = ::handleClickPrivacyDetail,
            onSubmit = ::handleSubmit,
        )
    }

    private fun handleClickAgreeAll() {
        val newCheck = !uiState.value.isAgreeAll
        _uiState.update {
            it.copy(isAgreeAge = newCheck, isAgreeService = newCheck, isAgreePrivacy = newCheck)
        }
    }

    private fun handleClickAgreeAge() {
        val newCheck = !uiState.value.isAgreeAge
        _uiState.update {
            it.copy(isAgreeAge = newCheck)
        }
    }

    private fun handleClickService() {
        val newCheck = !uiState.value.isAgreeService
        _uiState.update {
            it.copy(isAgreeService = newCheck)
        }
    }

    private fun handleClickPrivacy() {
        val newCheck = !uiState.value.isAgreePrivacy
        _uiState.update {
            it.copy(isAgreePrivacy = newCheck)
        }
    }

    private fun handleClickServiceDetail() {
        addViewEvent(ViewEvent.ToServiceDetail)
    }

    private fun handleClickPrivacyDetail() {
        addViewEvent(ViewEvent.ToPrivacyDetail)
    }

    private fun handleSubmit() {
        addViewEvent(ViewEvent.ToInfo)
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    sealed interface ViewEvent {
        object ToServiceDetail : ViewEvent
        object ToPrivacyDetail : ViewEvent
        object ToInfo : ViewEvent
    }
}
