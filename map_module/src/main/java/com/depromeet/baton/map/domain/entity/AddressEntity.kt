package com.depromeet.baton.map.domain.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressEntity(
    @SerializedName("address") val address: String,
    @SerializedName("roadAddress") val roadAddress: String
) : Parcelable
