package com.depromeet.baton.presentation.ui.sign

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.flowWithLifecycle
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSignUpAddressDetailBinding
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.sign.SignUpAddressDetailViewModel.ViewEvent
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@AndroidEntryPoint
class SignUpAddressDetailFragment :
    BaseFragment<FragmentSignUpAddressDetailBinding>(R.layout.fragment_sign_up_address_detail) {

    private val viewModel: SignUpAddressDetailViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by activityViewModels()

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
                ViewEvent.ToSignUp -> {
                    signUpViewModel.remember(viewModel)
                    signUpViewModel.signUp()
                }
                ViewEvent.ToHome -> {
                    MainActivity.start(requireContext())
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }

    companion object {
        fun newInstance(args: SignUpAddressDetailStartArgs): SignUpAddressDetailFragment {
            return SignUpAddressDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(SignUpAddressDetailViewModel.ARGS, args)
                }
            }
        }
    }
}

data class SignUpAddressDetailUiState(
    val roadAddress: String,
    val address: String,
    val detailAddress: String,
    val latitude: Float,
    val longitude: Float,
    val onDetailChanged: (Editable?) -> Unit,
    val onSubmit: () -> Unit,
) {
    val isEnabled = detailAddress.isNotBlank()
}

@HiltViewModel
class SignUpAddressDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val args = requireNotNull(savedStateHandle.get<SignUpAddressDetailStartArgs>(ARGS))

    private val _uiState: MutableStateFlow<SignUpAddressDetailUiState> =
        MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun createState(): SignUpAddressDetailUiState {
        return SignUpAddressDetailUiState(
            roadAddress = args.roadAddress,
            address = args.address,
            detailAddress = "",
            latitude = -1f,
            longitude = -1f,
            onDetailChanged = ::handleDetailChanged,
            onSubmit = ::handleSubmit
        )
    }

    private fun handleSubmit() {
        //BEAN: 지금까지 수집한 모든 정보를 가지고 실제 회원가입 요청을 해야한다.
        addViewEvent(ViewEvent.ToSignUp)
    }

    private fun handleDetailChanged(editable: Editable?) {
        _uiState.update { it.copy(detailAddress = editable.toString()) }
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    sealed interface ViewEvent {
        object ToSignUp: ViewEvent
        object ToHome : ViewEvent
    }

    companion object {
        const val ARGS = "startArgs"
    }
}

@Parcelize
data class SignUpAddressDetailStartArgs(
    val roadAddress: String,
    val address: String
) : Parcelable
