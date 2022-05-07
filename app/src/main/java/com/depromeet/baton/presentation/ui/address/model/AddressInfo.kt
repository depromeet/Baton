package com.depromeet.baton.presentation.ui.address.model

import com.depromeet.baton.map.domain.entity.AddressEntity

class AddressInfo ( val data : AddressEntity)  {
    var address :String = ""
    var roadAddress :String =""

    init {
        address = data.address
        roadAddress= data.roadAddress
    }
}

