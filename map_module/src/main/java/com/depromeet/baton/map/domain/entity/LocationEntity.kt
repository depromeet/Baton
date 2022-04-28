package com.depromeet.baton.map.domain.entity


import com.naver.maps.geometry.LatLng

class LocationEntity(_latitude :Double, _longitude :Double){
    var latitude :Double = 0.0
    var longitude  :Double = 0.0
    var address : AddressEntity= AddressEntity("","")
    var location : LatLng = LatLng(latitude, longitude)


    init{
        latitude = _latitude
        longitude = _longitude
        location= LatLng(latitude, longitude)
    }


    constructor(_latitude : Double,  _longitude :Double, _address : String, _roadAddress : String):this(_latitude, _longitude){
        this.latitude = _latitude
        this.longitude=_longitude
        this.address=AddressEntity(_address,_roadAddress)
        this.location= LatLng(latitude, longitude)
    }

    fun mapToUi() = this.address

}

