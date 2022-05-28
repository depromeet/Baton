package com.depromeet.baton.map.domain.entity

data class ShopEntity(
    val name : String,
    val link :String,
    val category: String,
    val location : LocationEntity
)