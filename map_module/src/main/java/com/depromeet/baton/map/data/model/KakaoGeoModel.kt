package com.depromeet.baton.map.data.model

import android.location.Location
import com.depromeet.baton.map.domain.entity.AddressEntity
import com.depromeet.baton.map.util.NetworkResult
import retrofit2.Response

class KakaoGeoModel(_lat : Double, _long: Double, _response: NetworkResult<KakaoGeoResponse>) {
    var response = _response
    var latitude =_lat
    var longitude =_long
    var roadAddress =""
    var address =""
    init{
        if(response.data?.meta!!.total_count !=0){
            roadAddress=_response.data!!.documents.get(0).road_address.address_name
            address=_response.data!!.documents.get(0).address.address_name
        }
    }

    fun mapToDomain(address : String?) : AddressEntity = AddressEntity(latitude, longitude,roadAddress)
}