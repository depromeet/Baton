package com.depromeet.baton.presentation.ui.sign

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentWelcomeBinding
import com.depromeet.baton.domain.api.user.SignApi
import com.depromeet.baton.domain.api.user.TokenApi
import com.depromeet.baton.domain.model.AuthInfo
import com.depromeet.baton.domain.model.LoginKakaoRequest
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.UserinfoRepository
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.sign.WelcomeViewModel.ViewEvent
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import com.depromeet.baton.util.loginWithKakao
import com.depromeet.baton.util.showToast
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(R.layout.fragment_welcome) {

    private val viewModel: WelcomeViewModel by viewModels()
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
                ViewEvent.ToHome -> {
                    MainActivity.start(requireContext())
                }
                is ViewEvent.ToSignUp -> {
                    with(viewEvent) { SignUpStartArgs(uid, nickname) }
                        .let { WelcomeFragmentDirections.actionWelcomeFragmentToSignUpFragment(it) }
                        .also { navController.navigate(it) }
                }
                ViewEvent.ToKakaoLogin -> {
                    lifecycleScope.launch {
                        try {
                            val oauthToken = UserApiClient.loginWithKakao(requireContext())
                            Timber.d("beanbean kakao token : $oauthToken")
                            viewModel.loginWithKakao(oauthToken.accessToken)
                        } catch (error: Throwable) {
                            showToast(error.message)
                            Timber.e(error)
                        }
                    }
                }
                is ViewEvent.ShowToast -> showToast(viewEvent.message)
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
class WelcomeViewModel @Inject constructor(
    private val signApi: SignApi,
    private val authRepository: AuthRepository,
    private val userinfoRepository: UserinfoRepository
) : BaseViewModel() {

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
    private fun authValidation(){
        viewModelScope.launch {
            userinfoRepository.authValidation(authRepository.authInfo?.accessToken!!,authRepository.authInfo?.refreshToken!!).let{
                Timber.e(it.toString())
                when(it){
                    is TokenApi.RefreshResult.Success ->{
                        authRepository.setAuthInfo(it.response.access_token!!,it.response.refresh_token!!)
                    }
                    is TokenApi.RefreshResult.Failure-> {
                        addViewEvent(ViewEvent.ShowToast(it.error.message ?: "알 수 없는 에러"))
                    }
                }
            }
        }
    }
    private fun handleNaverClick() {
        // 아직 없음.
    }

    private fun handleKakaoClick() {
        addViewEvent(ViewEvent.ToKakaoLogin)
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    fun loginWithKakao(kakaoAccessToken: String) {
        viewModelScope.launch {
            when (val result = signApi.signWithKakao(LoginKakaoRequest(kakaoAccessToken))) {
                is SignApi.KakaoLoginResult.Success -> {
                    val response = result.response
                    authRepository.authInfo = AuthInfo(
                        accessToken = response.accessToken,
                        refreshToken = response.refreshToken,
                        userId = response.user.id
                    )
                    addViewEvent(ViewEvent.ToHome)
                }
                is SignApi.KakaoLoginResult.NoSocialUser -> {
                    Timber.d(result.response.toString())
                    with(result.response) {
                        addViewEvent(ViewEvent.ToSignUp(uid, nickname))
                    }
                }
                is SignApi.KakaoLoginResult.Failure -> {
                    Timber.e(result.error)
                    authValidation()

                }
            }
        }
    }

    sealed interface ViewEvent {
        data class ToSignUp(val uid: String, val nickname: String?) : ViewEvent
        object ToHome : ViewEvent
        object ToKakaoLogin : ViewEvent
        data class ShowToast(val message: String) : ViewEvent
    }
}
