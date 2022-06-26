package com.depromeet.baton.map.data.model

import androidx.annotation.Keep
import com.depromeet.baton.map.data.model.response.KakaoGeoResponse
import com.depromeet.baton.map.domain.entity.LocationEntity
import com.depromeet.baton.map.util.NetworkResult
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
class KakaoGeoModel(_lat : Double, _long: Double, _response: NetworkResult<KakaoGeoResponse>) {
    var response = _response
    var latitude =_lat
    var longitude =_long
    var roadAddress =""
    var address =""
    init{
        if(response.data?.meta!!.total_count !=0){
            roadAddress =
                if( _response.data!!.documents.get(0).road_address !=null)
                    _response.data!!.documents.get(0).road_address!!.address_name
                else ""

            address=_response.data!!.documents.get(0).address.address_name
        }
    }

    fun mapToDomain() : LocationEntity = LocationEntity(latitude, longitude, address, roadAddress)
}