package com.depromeet.baton.presentation.ui.sign

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.annotation.Keep
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSignUpBinding
import com.depromeet.baton.domain.api.user.SignApi
import com.depromeet.baton.domain.api.user.SignApi.KakaoSignUpResult
import com.depromeet.baton.domain.model.AuthInfo
import com.depromeet.baton.domain.model.SignUpKakaoRequest
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.main.MainActivity
import com.depromeet.baton.presentation.ui.sign.SignUpViewModel.ViewEvent
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import com.depromeet.baton.util.BatonSpfManager
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.KProperty

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(R.layout.fragment_sign_up) {
    private val viewModel: SignUpViewModel by activityViewModels()
    private val args: SignUpFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.args = args.args

        viewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { uiState ->
                Timber.d("beanbean set ui state > $uiState")
                binding.uiState = uiState
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
                is ViewEvent.SignUpFailure -> {
                    Toast.makeText(context, viewEvent.message, Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }
}

@Keep
@Parcelize
data class SignUpStartArgs(
    val uid: String,
    val nickname: String?
) : Parcelable

data class SignUpUiState(
    val maxStep: Int,
    val currentStep: Int,
) {
    val title = when (currentStep) {
        1 -> "회원 가입"
        2 -> "회원 가입"
        3 -> "주소 설정"
        else -> ""
    }
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val signApi: SignApi,
    private val spfManager: BatonSpfManager,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    //    private val args = requireNotNull(savedStateHandle.get<SignUpStartArgs>("args"))
    lateinit var args: SignUpStartArgs

    private val _uiState: MutableStateFlow<SignUpUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private val builder by lazy { KakaoRequestBuilder(args.uid) }

    private fun createState(): SignUpUiState {
        return SignUpUiState(
            maxStep = 4,
            currentStep = 1
        )
    }

    fun setCurrentStep(currentStep: Int) {
        _uiState.update { it.copy(currentStep = currentStep) }
    }

    @Synchronized
    fun remember(termViewModel: SignUpTermViewModel) {
        with(termViewModel.uiState.value) {
            builder.apply {
                checkTermsOfService = isAgreeService
                checkPrivacyPolicy = isAgreePrivacy
            }
        }
    }

    @Synchronized
    fun remember(infoViewModel: SignUpInfoViewModel) {
        val source = infoViewModel.uiState.value
        builder.apply {
            name = source.name
            nickname = source.nickName.takeIf { it.isNotBlank() } ?: args.nickname
            phoneNumber = source.phoneNumber
        }
    }

    fun remember(addressViewModel: SignUpAddressViewModel) {
        //do nothing
    }

    @Synchronized
    fun remember(detailViewModel: SignUpAddressDetailViewModel) {
        with(detailViewModel.uiState.value) {
            //효민 local 저장했습니다
            spfManager.saveLocation(LatLng(this@with.addressData.latitude.toDouble() ,this@with.addressData.longitude.toDouble()))
            spfManager.saveDetailAddress(this@with.detailAddress)
            spfManager.saveAddress( this@with.addressData.roadAddress,  this@with.addressData.address)

            builder.apply {
                latitude = this@with.addressData.latitude
                longitude = this@with.addressData.longitude
                address = this@with.addressData.address
                detailedAddress = this@with.detailAddress
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            try {
                val request = builder.build()
                Timber.d("beanbean sign up request > $request")
                when (val result = signApi.signUpKakao(request)) {
                    is KakaoSignUpResult.Success -> {
                        Timber.d("beanbean sign up success > $result")
                        with(result.response) {
                            authRepository.authInfo = AuthInfo(
                                accessToken = accessToken,
                                refreshToken = refreshToken,
                                userId = user.id
                            )
                        }
                        addViewEvent(ViewEvent.ToHome)
                    }
                    is KakaoSignUpResult.Failure -> {
                        Timber.d("beanbean sign up failure > $result")
                        addViewEvent(ViewEvent.SignUpFailure(result.error.message.orEmpty()))
                    }
                }
            } catch (throwable: Throwable) {
                addViewEvent(ViewEvent.SignUpFailure(throwable.message.orEmpty()))
            }
        }
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    sealed class ViewEvent {
        object ToHome : ViewEvent()
        data class SignUpFailure(val message: String) : ViewEvent()
    }
}

private operator fun <T : Any> SavedStateHandle.getValue(
    thisRef: Any?,
    property: KProperty<*>
): T {
    return requireNotNull(this.get(property.name) as T?) { "${property.name} 을 못찾음." }
}

private class KakaoRequestBuilder(val uid: String) {
    var name: String? = null
    var nickname: String? = null
    var phoneNumber: String? = null
    var latitude: Float? = null
    var longitude: Float? = null
    var address: String? = null
    var detailedAddress: String? = null
    var checkTermsOfService: Boolean? = null
    var checkPrivacyPolicy: Boolean? = null
    var account: SignUpKakaoRequest.Account? = null

    fun build(): SignUpKakaoRequest {
        return SignUpKakaoRequest(
            uid = uid,
            provider= "kakao",
            user = SignUpKakaoRequest.User(
                name = checkNotNull(name),
                nickname = checkNotNull(nickname),
                phoneNumber = checkNotNull(phoneNumber),
                latitude = checkNotNull(latitude),
                longitude = checkNotNull(longitude),
                address = checkNotNull(address),
                detailedAddress = checkNotNull(detailedAddress),
                checkTermsOfService = checkNotNull(checkTermsOfService),
                checkPrivacyPolicy = checkNotNull(checkPrivacyPolicy),
                account = account
            )
        )
    }
}
