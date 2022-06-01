package com.depromeet.baton.presentation.ui.sign

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentWelcomeBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.sign.WelcomeViewModel.ViewEvent
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {

    private val viewModel: WelcomeViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { uiState ->
                binding.buttonKakao.setOnClickListener { uiState.onKakaoClick() }
                binding.buttonNaver.setOnClickListener { uiState.onNaverClick() }
            }
            .launchIn(viewLifecycleScope)

        viewModel.viewEvents
            .flowWithLifecycle(viewLifecycle)
            .onEach(::handleViewEvents)
            .launchIn(viewLifecycleScope)
    }

    private fun handleViewEvents(viewEvents: List<ViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                ViewEvent.ToHome -> {
                    MainActivity.start(requireContext())
                }
                ViewEvent.ToSignUp -> {
                    navController.navigate(R.id.action_welcomeFragment_to_signUpFragment)
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }
}

data class WelcomeUiState(
    val onKakaoClick: () -> Unit,
    val onNaverClick: () -> Unit,
)

@HiltViewModel
class WelcomeViewModel @Inject constructor() : BaseViewModel() {

    private val _uiState: MutableStateFlow<WelcomeUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun createState(): WelcomeUiState {
        return WelcomeUiState(
            onKakaoClick = { handleKakaoClick() },
            onNaverClick = { handleNaverClick() }
        )
    }

    //BEAN: 일단 무조건 회원가입 루틴으로 이동하는 중.
    private fun handleNaverClick() {
        addViewEvent(ViewEvent.ToSignUp)
    }

    private fun handleKakaoClick() {
        addViewEvent(ViewEvent.ToSignUp)
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    sealed interface ViewEvent {
        object ToSignUp : ViewEvent
        object ToHome : ViewEvent
    }
}
