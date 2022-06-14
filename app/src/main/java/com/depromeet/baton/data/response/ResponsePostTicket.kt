package com.depromeet.baton.data.response


data class ResponsePostTicket(
    val createAt: String,
    val expiryDate: String?,
    val id: Int,
    val location: String?,
    val address: String?,
    val price: Int,
    val mainImage: String?,
    val tags: List<String>?,
    val isMembership: Boolean?,
    val remainingNumber: Int?,
    val remainingDay: Int?,
    val state: String?,
    val latitude: Double,
    val longitude: Double,
    val distance: Double?
)