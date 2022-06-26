package com.depromeet.baton.presentation.ui.sign

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.depromeet.baton.R
import com.depromeet.baton.databinding.FragmentSignUpAddressBinding
import com.depromeet.baton.map.domain.usecase.SearchAddressUseCase
import com.depromeet.baton.presentation.base.BaseFragment
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.address.SearchAddressAdapter
import com.depromeet.baton.presentation.ui.address.model.AddressInfo
import com.depromeet.baton.presentation.ui.sign.SignUpAddressViewModel.ViewEvent
import com.depromeet.baton.presentation.util.viewLifecycle
import com.depromeet.baton.presentation.util.viewLifecycleScope
import com.depromeet.baton.util.gpsConverter
import com.depromeet.baton.util.showToast
import com.depromeet.bds.component.BdsSearchBar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SignUpAddressFragment :
    BaseFragment<FragmentSignUpAddressBinding>(R.layout.fragment_sign_up_address) {

    private val viewModel: SignUpAddressViewModel by viewModels()
    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private val navController by lazy { findNavController() }
    private val adapter by lazy {
        SearchAddressAdapter { viewModel.handleAddressClick(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signUpViewModel.setCurrentStep(3)

        binding.searchAddressEt.textListener = object : BdsSearchBar.DefaultTextListener() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.handleKeywordChanged(s)
            }
        }

        binding.searchAddressRecycler.adapter = adapter

        viewModel.uiState
            .flowWithLifecycle(viewLifecycle)
            .onEach { uiState ->
                binding.uiState = uiState
                adapter.submitList(uiState.addressInfo)
            }
            .launchIn(viewLifecycleScope)

        viewModel.viewEvents
            .flowWithLifecycle(viewLifecycle)
            .onEach(::handleViewEvents)
            .launchIn(viewLifecycleScope)
    }

    private val currentLocationLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val resultArgs = it.data!!
                        .getParcelableExtra<SignUpCurrentLocationActivity.ResultArgs>(
                            SignUpCurrentLocationActivity.RESULT_ARGS
                        )!!

                    navigateToDetail(resultArgs.addressData)
                } catch (e: Throwable) {
                    Timber.e(e)
                    showToast(e.message)
                }
            }
        }

    private fun handleViewEvents(viewEvents: List<ViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                ViewEvent.ToCurrentLocationSetting -> {
                    val intent = SignUpCurrentLocationActivity.intent(context)
                    currentLocationLauncher.launch(intent)
                }
                is ViewEvent.ToAddressDetailSetting -> {
                    signUpViewModel.remember(viewModel)
                    navigateToDetail(viewEvent.addressData)
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }

    private fun navigateToDetail(addressData: AddressData) {
        SignUpAddressFragmentDirections
            .actionSignUpAddressFragmentToSignUpAddressDetailFragment(
                SignUpAddressDetailStartArgs(addressData)
            )
            .also { navController.navigate(it) }
    }
}

data class SignUpAddressUiState(
    val keyword: String,
    val addressInfo: List<AddressInfo>,
    val isAddressLoading: Boolean,
    val onSetCurrentLocationClick: () -> Unit,
    val onKeywordChanged: (Editable?) -> Unit,
    val onAddressClick: (AddressInfo) -> Unit,
) {
    val isTipVisible = keyword.isBlank()
    val isAddressListVisible = !isAddressLoading && addressInfo.isNotEmpty()
    val isAddressNotFoundVisible =
        !isAddressLoading && keyword.isNotBlank() && addressInfo.isEmpty()
}

@OptIn(FlowPreview::class)
@HiltViewModel
class SignUpAddressViewModel @Inject constructor(
    application : Application,
    private val searchAddressUseCase: SearchAddressUseCase,
) : AndroidViewModel(application){

    private val _uiState: MutableStateFlow<SignUpAddressUiState> = MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private fun createState(): SignUpAddressUiState {
        return SignUpAddressUiState(
            keyword = "",
            addressInfo = emptyList(),
            isAddressLoading = false,
            onSetCurrentLocationClick = ::handleSetCurrentLocationClick,
            onKeywordChanged = ::handleKeywordChanged,
            onAddressClick = ::handleAddressClick,
        )
    }

    fun handleAddressClick(addressInfo: AddressInfo) {
        val location = gpsConverter(getApplication(), addressInfo.roadAddress)
        addViewEvent(
            ViewEvent.ToAddressDetailSetting(
                //TODO: Latitude... longitude ...
                AddressData(
                    addressInfo.roadAddress,
                    addressInfo.address,
                    latitude= location.latitude.toFloat(),
                    longitude = location.longitude.toFloat()
                )
            )
        )
    }

    private fun handleSetCurrentLocationClick() {
        addViewEvent(ViewEvent.ToCurrentLocationSetting)
    }

    fun handleKeywordChanged(editable: Editable?) {
        _uiState.update {
            val newValue = editable.toString()
            if (newValue.isBlank()) {
                it.copy(keyword = editable.toString(), addressInfo = emptyList())
            } else {
                it.copy(keyword = editable.toString())
            }
        }
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    sealed interface ViewEvent {
        object ToCurrentLocationSetting : ViewEvent
        data class ToAddressDetailSetting(val addressData: AddressData) : ViewEvent
    }

    init {

        initSearchAddress()
    }

    private fun initSearchAddress() {
        uiState
            .map { it.keyword.trim() }
            .distinctUntilChanged()
            .debounce(200L)
            .onEach {
                _uiState.update { it.copy(isAddressLoading = true) }
            }
            .transformLatest {
                // address를 검색할 거고, 중간에 다른 keyword가 emit 된다면 cancel 될 것이다.
                emit(searchAddressUseCase.suspendSearchAddress(it))
            }
            .onEach { list ->
                _uiState.update {
                    it.copy(
                        addressInfo = list.map(::AddressInfo),
                        isAddressLoading = false
                    )
                }
            }
            .catch {
                _uiState.update {
                    it.copy(
                        addressInfo = emptyList(),
                        isAddressLoading = false
                    )
                }
            }
            .flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }
}
