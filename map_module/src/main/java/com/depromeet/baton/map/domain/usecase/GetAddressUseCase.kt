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

    suspend fun getMyAddress() =repository.getMyAddress()

    suspend fun gpsConverter( location : LatLng) = repository.GPStoAddress(location)

    fun stopLocationUpdate(){
        repository.stopAddressRequest()
    }

}