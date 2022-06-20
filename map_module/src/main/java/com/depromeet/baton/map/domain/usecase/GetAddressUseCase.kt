package com.depromeet.baton.map.domain.usecase

import com.depromeet.baton.map.domain.entity.AddressEntity
import com.depromeet.baton.map.domain.entity.LocationEntity
import com.depromeet.baton.map.domain.repository.AddressRepository
import com.depromeet.baton.map.domain.repository.SearchAddressRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.map.util.NetworkResultMapper
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

class GetAddressUseCase @Inject constructor(
    private val repository : AddressRepository,
    private val searchAddressRepository: SearchAddressRepository
    ){

    suspend fun getMyAddress()= flow{
        repository.getMyAddress().collect{ res->
            when(res){
                is NetworkResult.Success->{
                    if(res.data!!.address.roadAddress==""){
                        searchAddressRepository.searchAddress(res.data.address.address).collect{
                            if(it is NetworkResult.Success){
                                Timber.d("hyomin ${it.data!!.item}")
                                if(it.data.item.isNotEmpty()) res.data.apply{
                                    this.address = AddressEntity(res.data.address.address, it.data!!.item[0].roadAddress)
                                    stopLocationUpdate()
                                }
                                emit(res)
                            }
                        }
                    }
                   else emit(res)
                }
                else -> emit(res)
            }
        }
    }

    suspend fun gpsConverter( location : LatLng) = repository.GPStoAddress(location)

    fun stopLocationUpdate(){
        repository.stopAddressRequest()
    }

}