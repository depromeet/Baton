package com.depromeet.baton.domain.model

import com.squareup.moshi.Json

data class TicketSimpleInfo(
    @Json(name="id") val id : Int,
    @Json(name="location") val location: String,
    @Json(name="address") val address: String,
    @Json(name="price") val price: Int,
    @Json(name="mainImage")  val mainImage: String?,
    @Json(name="createAt") val createAt: String,
    @Json(name="state") val state: Int,
    @Json(name="tags") val tags: List<String>,
    @Json(name="images") val images: List<Any> ? = emptyList(),
    @Json(name="isMembership")  val isMembership: Boolean,
    @Json(name="remainNumber") val remainingNumber: Int?=-1,
    @Json(name="expiryDate")  val expiryDate: String?="",
    @Json(name="latitude")  val latitude: Double,
    @Json(name="longitude")  val longitude: Double,
    @Json(name="distance")  val distance: Double,
    val isBookmark : Boolean?= false
) {
    data class Image(
        val id: Int?=0,
        val url: String?="",
        val thumbnailUrl: String?="",
        val isMain: Boolean?=false
    )
}