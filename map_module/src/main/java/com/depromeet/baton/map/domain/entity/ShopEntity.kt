package com.depromeet.baton.map.domain.entity

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
data class ShopEntity(
    val name : String,
    val link :String,
    val category: String,
    val location : LocationEntity
)