package com.depromeet.baton.presentation.ui.sign

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivitySignUpCurrentLocationBinding
import com.depromeet.baton.map.domain.usecase.GetAddressUseCase
import com.depromeet.baton.presentation.base.BaseActivity
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.address.view.AddressActivity.Companion.permissions
import com.depromeet.baton.presentation.ui.sign.SignUpCurrentLocationViewModel.ViewEvent
import com.depromeet.baton.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SignUpCurrentLocationActivity :
    BaseActivity<ActivitySignUpCurrentLocationBinding>(R.layout.activity_sign_up_current_location) {

    private val viewModel: SignUpCurrentLocationViewModel by viewModels()
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { granted ->
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.uiState
            .flowWithLifecycle(lifecycle)
            .onEach { uiState -> binding.uiState = uiState }
            .launchIn(lifecycleScope)
        viewModel.viewEvents
            .flowWithLifecycle(lifecycle)
            .onEach(::handleViewEvents)
            .launchIn(lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        fetchLocation()
    }

    private fun fetchLocation() {
        when {
            isPermissionGranted(permissions[0]) || isPermissionGranted(permissions[1]) -> {
                viewModel.fetchLocation()
            }
            else -> {
                permissionLauncher.launch(permissions)
            }
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun handleViewEvents(viewEvents: List<ViewEvent>) {
        viewEvents.firstOrNull()?.let { viewEvent ->
            when (viewEvent) {
                is ViewEvent.Complete -> {
                    val resultArgs = ResultArgs(viewEvent.addressData)
                    setResult(
                        Activity.RESULT_OK,
                        Intent().apply {
                            putExtra(RESULT_ARGS, resultArgs)
                        }
                    )
                    finish()
                }
                is ViewEvent.ShowToast -> {
                    showToast(viewEvent.message)
                }
            }
            viewModel.consumeViewEvent(viewEvent)
        }
    }

    @Parcelize
    data class ResultArgs(
        val addressData: AddressData
    ) : Parcelable

    companion object {
        const val RESULT_ARGS = "result_args"

        fun intent(context: Context?) = Intent(context, SignUpCurrentLocationActivity::class.java)
    }
}

data class SignUpCurrentLocationUiState(
    val isLoading: Boolean,
    val addressData: AddressData?,
    val onSubmit: () -> Unit,
) {
    val isEnabled = !isLoading
}

@HiltViewModel
class SignUpCurrentLocationViewModel @Inject constructor(
    private val addressUseCase: GetAddressUseCase,
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<SignUpCurrentLocationUiState> =
        MutableStateFlow(createState())
    val uiState = _uiState.asStateFlow()

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private var fetchingLocationJob: Job? = null

    private fun createState(): SignUpCurrentLocationUiState {
        return SignUpCurrentLocationUiState(
            isLoading = false,
            addressData = null,
            onSubmit = ::handleSubmit
        )
    }

    private fun handleSubmit() {
        //TODO: ?
        Timber.d("beanbean handleSubmit > ${uiState.value.addressData}")
        uiState.value.addressData?.let { addViewEvent(ViewEvent.Complete(it)) }
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    fun fetchLocation() {
        fetchingLocationJob?.cancel()
        fetchingLocationJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val myAddress = addressUseCase.getMyAddress().first()

                val addressData = myAddress.data?.let {
                    AddressData(
                        roadAddress = it.address.roadAddress,
                        address = it.address.address,
                        latitude = it.latitude.toFloat(),
                        longitude = it.longitude.toFloat()
                    )
                }!!

                _uiState.update { it.copy(isLoading = false, addressData = addressData) }
            } catch (e: Throwable) {
                Timber.e(e)
                addViewEvent(ViewEvent.ShowToast(e.message))
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    sealed class ViewEvent {
        class ShowToast(val message: String?) : ViewEvent()
        class Complete(val addressData: AddressData) : ViewEvent()
    }
}
