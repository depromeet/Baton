package com.depromeet.baton.map.domain.entity

class AddressEntity ( _address : String, _roadAddress: String) {
    var address :String = ""
    var roadAddress :String =""

    init {
        address = _address
        roadAddress=_roadAddress
    }
}