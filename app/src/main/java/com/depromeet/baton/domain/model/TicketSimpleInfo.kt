package com.depromeet.baton.domain.model

import androidx.annotation.Keep
import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.squareup.moshi.Json

@Keep
data class TicketSimpleInfo(
    @Json(name="id") val id : Int,
    @Json(name="location") val location: String,
    @Json(name="address") val address: String,
    @Json(name="price") val price: Int,
    @Json(name="mainImage")  val mainImage: String?,
    @Json(name="createAt") val createAt: String,
    @Json(name="state") val state: String,
    @Json(name="tags") val tags: List<String>,
    @Json(name="images") val images: List<Image> ? = emptyList(),
    @Json(name="isMembership")  val isMembership: Boolean,
    @Json(name="remainingNumber") val remainingNumber: Int?=-1,
    @Json(name="remainingDay") val remainingDay: Int?= -1,
    @Json(name="expiryDate")  val expiryDate: String?="",
    @Json(name="latitude")  val latitude: Double,
    @Json(name="longitude")  val longitude: Double,
    @Json(name="distance")  val distance: Double,
    @Json(name="bookmarkId")  val bookmarkId : Int?,
    @Json(name="bookmarkCount")  val bookmarkCount : Int,
    @Json(name="viewCount")  val viewCount: Int,
    @Json(name="type")  val type : String,

) {
    @Keep
    data class Image(
        @Json(name="id")val id: Int?=0,
        @Json(name="url")val url: String?="",
        @Json(name="thumbnailUrl") val thumbnailUrl: String?="",
        @Json(name="isMain")val isMain: Boolean?=false
    )
}