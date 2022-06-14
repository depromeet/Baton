package com.depromeet.baton.data.response

import com.squareup.moshi.Json

data class TicketSearchResponse (
    @Json(name="content") val content :List<ResponseTicket>,

){
    data class ResponseTicket(
        val id: Int,
        val location: String,
        val address: String,
        val price: Int,
        val mainImage: String?="",
        val images :List<Image>,
        val createAt: String,
        val state: String,
        val tags: List<String>,
        val isMembership: Boolean,
        val remainingNumber: Int,
        val expiryDate: String,
        val latitude: Double,
        val longitude: Double,
        val distance: Double
    ) {
        data class Image(
            val id: Int,
            val url: String,
            val thumbnailUrl: String,
            val isMain: Boolean
        )
    }

}