package com.depromeet.baton.presentation.ui.address

import androidx.lifecycle.*
import com.depromeet.baton.map.domain.entity.AddressEntity
import com.depromeet.baton.map.domain.usecase.GetAddressUseCase
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.map.util.UiState
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddressViewModel  @Inject constructor(
    private val addressUseCase: GetAddressUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _uiState = MutableStateFlow<UiState<AddressEntity>>(UiState.Loading())
    val uiState: StateFlow<UiState<AddressEntity>>
        get() = _uiState

    private val _address : MutableLiveData<NetworkResult<AddressEntity>> = MutableLiveData()
    val address: LiveData<NetworkResult<AddressEntity>> = _address

    //현재 내 위치 정보 받아오기
    fun getMyAddress()= viewModelScope.launch {
        _address.value = NetworkResult.Loading()
        addressUseCase.getMyAddress().collect{
                values ->
            when(values){
                is NetworkResult.Success ->{
                    Timber.e("${values.data?.address } ,${values.data?.roadAddress }")
                    _address.value =values
                    _uiState.value = UiState.Success(values.data!!)
                    addressUseCase.stopLocationUpdate()
                }
                is NetworkResult.Error ->{
                    Timber.e(values.message)
                }
                is NetworkResult.Loading->{
                    _uiState.value = UiState.Loading()
                }
            }
        }
    }

    fun gpsToAddress (location : LatLng) = viewModelScope.launch {
        _address.value = NetworkResult.Loading()
        addressUseCase.gpsConverter(location).collect{
                values ->
            when(values){
                is NetworkResult.Success ->{
                    Timber.e("${values.data?.address}")
                    _address.value =values
                    _uiState.value = UiState.Success(values.data!!)
                    addressUseCase.stopLocationUpdate()
                }
                is NetworkResult.Error ->{
                    Timber.e(values.message)
                }
                is NetworkResult.Loading->{
                    _uiState.value = UiState.Loading()
                }
            }
        }
    }

}
