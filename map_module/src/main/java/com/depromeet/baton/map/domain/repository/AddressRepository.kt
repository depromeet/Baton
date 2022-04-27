package com.depromeet.baton.map.domain.repository


import com.depromeet.baton.map.domain.entity.AddressEntity
import com.depromeet.baton.map.util.NetworkResult
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.Flow

interface AddressRepository {
    suspend fun getMyAddress() : Flow<NetworkResult<AddressEntity>>
    suspend fun getLocation() : Flow<NetworkResult<AddressEntity>>
    suspend fun GPStoAddress(location : LatLng): Flow<NetworkResult<AddressEntity>>
   // suspend fun AddresstoGPS(address : String) : Flow<NetworkResult<LocationEntity>> //TODO ADDRESS ->GPS
    fun stopAddressRequest() : Unit
}