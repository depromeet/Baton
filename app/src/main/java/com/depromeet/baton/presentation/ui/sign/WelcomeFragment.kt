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
import com.depromeet.baton.domain.model.AuthInfo
import com.depromeet.baton.domain.model.LoginKakaoRequest
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.sign.WelcomeViewModel.ViewEvent
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import com.depromeet.baton.util.loginWithKakao
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
                ViewEvent.ToSignUp -> {
                    navController.navigate(R.id.action_welcomeFragment_to_signUpFragment)
                }
                ViewEvent.ToKakaoLogin -> {
                    lifecycleScope.launch {
                        try {
                            val oauthToken = UserApiClient.loginWithKakao(requireContext())
                            Timber.d("beanbean kakao token : $oauthToken")
                            viewModel.loginWithKakao(oauthToken.accessToken)
                        } catch (error: Throwable) {
                            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT)
                                .show()
                            Timber.e(error)
                        }
                    }
                }
                is ViewEvent.ShowToast -> {
                    Toast.makeText(requireContext(), viewEvent.message, Toast.LENGTH_SHORT).show()
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
class WelcomeViewModel @Inject constructor(
    private val signApi: SignApi,
    private val authRepository: AuthRepository
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

    //BEAN: 일단 무조건 회원가입 루틴으로 이동하는 중.
    private fun handleNaverClick() {
        addViewEvent(ViewEvent.ToSignUp)
    }

    private fun handleKakaoClick() {
        // 카카오 인증 서버로 인가 코드 발급 요청
        // 카카오 서비스가 인가 코드를 발급하고 제공하게 될 것 (authorization code)
        // 이 인가 코드로 토큰을 요청하고 받음 (access token)
        // --------------------
        // 토큰을 서버로 보냄
        //   기존에 계정 정보가 있다면 Baton Access Token 을 받음
        //   없다면? -> 추가 정보를 받아야함.

        addViewEvent(ViewEvent.ToKakaoLogin)
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    fun loginWithKakao(kakaoAccessToken: String) {

        //TODO:
        // - 추가 정보를 포함한 로그인 요청
        // - 로그인이 필요한 경우 어떻게 알 수 있음?
        viewModelScope.launch {
            try {
                val response = signApi.signWithKakao(LoginKakaoRequest(kakaoAccessToken))

                authRepository.setAuthInfo(
                    AuthInfo(
                        accessToken = response.accessToken,
                        refreshToken = response.refreshToken,
                        userId = response.user.id
                    )
                )

                addViewEvent(ViewEvent.ToHome)
            } catch (error: Throwable) {
                //BEAN: 에러 코드에 따라서 다른 반응.
                // 기존에 계정 정보가 없다면? -> 로그인 페이지로 랜딩
                // 그 외의 에러라면? -> 에러 노출
                addViewEvent(ViewEvent.ShowToast(error.message ?: "알 수 없는 에러"))
            }
        }
    }

    sealed interface ViewEvent {
        object ToSignUp : ViewEvent
        object ToHome : ViewEvent
        object ToKakaoLogin : ViewEvent
        data class ShowToast(val message: String) : ViewEvent
    }
}
