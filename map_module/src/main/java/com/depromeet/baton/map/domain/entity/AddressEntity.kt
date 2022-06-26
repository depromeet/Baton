package com.depromeet.baton.map.domain.entity

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
@Keep
@JsonClass(generateAdapter = false)
@Parcelize
data class AddressEntity(
    @SerializedName("address") val address: String,
    @SerializedName("roadAddress") val roadAddress: String,
) : Parcelable
