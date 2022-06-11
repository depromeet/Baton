package com.depromeet.baton.domain.model

import com.squareup.moshi.Json

data class TicketInfo(
    @Json(name="id") val id : Int,
    @Json(name="seller") val seller : Seller,
    @Json(name="location") val location: String,
    @Json(name="address") val address: String,
    @Json(name="price") val price: Int,
    @Json(name="mainImage")  val mainImage: String?,
    @Json(name="createAt") val createAt: String,
    @Json(name="expiryDate")  val expiryDate: String?="",
    @Json(name="state") val state: String,
    @Json(name="type") val type: String,
    @Json(name="transferType") val tradeType: String,
    @Json(name="transferFee") val transferFee: String,
    @Json(name="canNego") val canNego: Boolean,
    @Json(name="hasShower")val hasShower: Boolean,
    @Json(name="hasLocker")val hasLocker: Boolean,
    @Json(name="hasClothes")val hasClothes: Boolean,
    @Json(name="hasGx")val hasGx: Boolean,
    @Json(name="canResell")val canResell: Boolean,
    @Json(name="canRefund") val canRefund: Boolean,
    @Json(name="description")val description: String,
    @Json(name="tags") val tags: List<String>,
    @Json(name="images") val images: List<Any> ? = emptyList(),
    @Json(name="isMembership")  val isMembership: Boolean,
    @Json(name="remainNumber") val remainingNumber: Int?=-1,
    @Json(name="latitude")  val latitude: Double,
    @Json(name="longitude")  val longitude: Double,
    @Json(name="distance")  val distance: Double,
) {
    data class Image(
        val id: Int?=0,
        val url: String?="",
        val thumbnailUrl: String?="",
        val isMain: Boolean?=false
    )
    data class Seller(
        val id: Int,
        val nickname: String,
        val gender: Boolean
    )
}