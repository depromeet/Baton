package com.depromeet.baton.map.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class AddressEntity (val _address : String, val _roadAddress: String) : Parcelable {
    @SerializedName("address") var address :String = ""
    @SerializedName("roadAddress")var roadAddress :String =""
    init {
        address = _address
        roadAddress=_roadAddress
    }
}