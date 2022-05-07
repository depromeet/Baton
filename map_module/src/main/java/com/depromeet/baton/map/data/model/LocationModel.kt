package com.depromeet.baton.map.data.model

import android.location.Location
import com.depromeet.baton.map.domain.entity.LocationEntity


class LocationModel {
    val location : Location
    val longitude : Double
    val latitude : Double

    constructor(locationParam : Location){
        location=locationParam
        longitude=location.longitude
        latitude=location.latitude
    }
    fun mapToDomain() : LocationEntity = LocationEntity(location.latitude, location.longitude)

}