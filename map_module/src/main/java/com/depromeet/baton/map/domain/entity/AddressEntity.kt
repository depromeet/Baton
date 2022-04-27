package com.depromeet.baton.map.domain.entity


import com.naver.maps.geometry.LatLng

class AddressEntity(_latitude :Double, _longitude :Double){
    var latitude :Double = 0.0
    var longitude  :Double = 0.0
    var address :String = ""
    var location : LatLng = LatLng(latitude, longitude)


    init{
        latitude = _latitude
        longitude = _longitude
        address  = ""
        location= LatLng(latitude, longitude)
    }


    constructor(_latitude : Double,  _longitude :Double, _address : String):this(_latitude, _longitude){
        this.latitude = _latitude
        this.longitude=_longitude
        this.address=_address
        this.location= LatLng(latitude, longitude)
    }

    fun mapToUi() = this.address

}