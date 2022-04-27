package com.depromeet.baton.map.data.model

import android.location.Location
import com.depromeet.baton.map.domain.entity.AddressEntity

class LocationModel {
    val location : Location
    val longitude : Double
    val latitude : Double

    constructor(locationParam : Location){
        location=locationParam
        longitude=location.longitude
        latitude=location.latitude
    }
    fun mapToDomain() : AddressEntity = AddressEntity(location.latitude, location.longitude)

}