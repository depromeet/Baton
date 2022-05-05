package com.depromeet.baton.presentation.ui.address.viewmodel

import androidx.lifecycle.*
import com.depromeet.baton.map.domain.entity.AddressEntity
import com.depromeet.baton.map.domain.usecase.GetAddressUseCase
import com.depromeet.baton.map.domain.usecase.SearchAddressUseCase
import com.depromeet.baton.map.domain.usecase.SearchItem
import com.depromeet.baton.map.util.Event
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.util.getAddress
import com.depromeet.baton.util.saveAddress
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyLocationViewModel  @Inject constructor(
    private val addressUseCase: GetAddressUseCase,
    private val searchAddressUseCase: SearchAddressUseCase,
) : ViewModel(){


    val _uiState =  MutableLiveData<UIState?>(UIState.Init)
    val uiState :LiveData<UIState?> get()=_uiState

    private val _roadState = MutableLiveData<String>("도로명 주소")
    val roadState : LiveData<String> get()=_roadState

    private val _jibunState = MutableLiveData<String>("[지번]")
    val jibunState : LiveData<String> get()=_jibunState

    private val _res : MutableLiveData<NetworkResult<AddressEntity>> = MutableLiveData()
    val res: LiveData<NetworkResult<AddressEntity>> = _res

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText


    init{
        _roadState.value =  if( getAddress().roadAddress == "") "도로명 주소" else  getAddress().roadAddress
        _jibunState.value =  if( getAddress().address == "") "[지번]" else   "[지번]"+getAddress().address

    }

    //현재 내 위치 정보 받아오기
    fun getMyAddress()= viewModelScope.launch {
        _res.value = NetworkResult.Loading()
        _uiState.value = (UIState.Loading)
        addressUseCase.getMyAddress().collect{
                values ->
            when(values){
                is NetworkResult.Success ->{
                    Timber.e("${values.data?.address } ,${values.data?.roadAddress }")
                    _res.value =values
                    if(values.data!!.roadAddress ==""){
                        getRoadAddress(values.data!!.address)
                    }else{
                        _roadState.value =values.data!!.roadAddress
                        _jibunState.value="[지번]${values.data!!.address}"
                        _uiState.value = (UIState.HasData)
                        addressUseCase.stopLocationUpdate()
                        saveAddress(values.data!!.roadAddress, values.data!!.address)
                    }
                }
                is NetworkResult.Error ->{
                    Timber.e(values.message)
                    _uiState.value =(UIState.Loading)

                    //TODO : 에러처리
                }
                is NetworkResult.Loading->{
                    _uiState.value =(UIState.Loading)
                }
            }
        }
    }


    private fun getRoadAddress(jibun : String){
        viewModelScope.launch {
            searchAddressUseCase.searchAddress(jibun).collect{
                when(it){
                    is SearchItem.Content ->{
                        _roadState.value= it.data!!.get(0).roadAddress
                        _jibunState.value="[지번]${jibun}"
                        _uiState.value = (UIState.HasData)
                    }
                    is SearchItem.Empty->{
                        _uiState.value= (UIState.NoData)
                        _snackbarText.value =Event("위치정보를 찾을 수 없습니다")
                    }
                }
                addressUseCase.stopLocationUpdate()
            }
        }
    }


    fun gpsToAddress (location : LatLng) = viewModelScope.launch {
        _uiState.value =(UIState.Loading)
        addressUseCase.gpsConverter(location).collect{
                values ->
            when(values){
                is NetworkResult.Success ->{
                    if(values.data!!.roadAddress ==""){
                        getRoadAddress(values.data!!.address)
                    }else{
                        _roadState.value =values.data!!.roadAddress
                        _jibunState.value=values.data!!.address
                        _uiState.value =(UIState.HasData)
                    }
                }
                is NetworkResult.Error ->{
                    Timber.e(values.message)
                }
                is NetworkResult.Loading->{
                    _uiState.value =(UIState.Loading)
                }
            }
        }
    }

}
