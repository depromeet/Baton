package com.depromeet.baton.data.response


import com.google.gson.annotations.SerializedName

data class ResponsePostTicket(
    val createAt: String,
    val expiryDate: String,
    val id: Int,
    val location: String?,
    val address: String?,
    val price: Int?,
    val mainImage: String?,
    val tags: List<String>,
    val isMembership: Boolean?,
    val remainingNumber: Int?,
    val remainingDay: Any?,
    val state: String?,
    val latitude: Double?,
    val longitude: Double?,
    val distance: Any?
)