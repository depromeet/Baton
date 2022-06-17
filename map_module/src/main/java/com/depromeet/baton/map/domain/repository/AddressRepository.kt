package com.depromeet.baton.map.domain.repository


import com.depromeet.baton.map.domain.entity.LocationEntity
import com.depromeet.baton.map.util.NetworkResult
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    suspend fun getMyAddress() : Flow<NetworkResult<LocationEntity>>
    suspend fun getLocation() : Flow<NetworkResult<LocationEntity>>
    suspend fun GPStoAddress(location : LatLng): Flow<NetworkResult<LocationEntity>>
    fun stopAddressRequest() : Unit
}