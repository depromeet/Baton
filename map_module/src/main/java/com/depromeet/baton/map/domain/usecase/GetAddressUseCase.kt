package com.depromeet.baton.map.domain.usecase

import com.depromeet.baton.map.domain.entity.AddressEntity
import com.depromeet.baton.map.domain.entity.LocationEntity
import com.depromeet.baton.map.domain.repository.AddressRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.map.util.NetworkResultMapper
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(private val repository : AddressRepository){

    suspend fun getMyAddress() =repository.getMyAddress().map {
            it ->  AddressMapper().mapper(it)//todo toUiModel() 구현
    }.catch { e ->
        Timber.e(e)
    }

    suspend fun gpsConverter( location : LatLng) = repository.GPStoAddress(location).map {
            it ->  AddressMapper().mapper(it)//todo toUiModel() 구현
    }.catch { e ->
        Timber.e(e)
    }

    fun stopLocationUpdate(){
        repository.stopAddressRequest()
    }

    inner class AddressMapper() : NetworkResultMapper<LocationEntity,AddressEntity>{
        override fun mapper(input: NetworkResult<LocationEntity>): NetworkResult<AddressEntity> {
            if(input is NetworkResult.Success) input.data?.let {  return NetworkResult.Success<AddressEntity>(it.mapToUi()) }
            return NetworkResult.Error<AddressEntity>(input.message.toString())
        }
    }
}