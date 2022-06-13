package com.depromeet.baton.data.response


import com.google.gson.annotations.SerializedName

data class d(
    @SerializedName("createAt")
    val createAt: String,
    @SerializedName("expiryDate")
    val expiryDate: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("seller")
    val seller: Seller,
    @SerializedName("location")
    val location: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("state")
    val state: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("tradeType")
    val tradeType: String,
    @SerializedName("transferFee")
    val transferFee: String,
    @SerializedName("isBookmarked")
    val isBookmarked: Boolean,
    @SerializedName("canNego")
    val canNego: Boolean,
    @SerializedName("hasShower")
    val hasShower: Boolean,
    @SerializedName("hasLocker")
    val hasLocker: Boolean,
    @SerializedName("hasClothes")
    val hasClothes: Boolean,
    @SerializedName("hasGx")
    val hasGx: Boolean,
    @SerializedName("canResell")
    val canResell: Boolean,
    @SerializedName("canRefund")
    val canRefund: Boolean,
    @SerializedName("description")
    val description: String,
    @SerializedName("tags")
    val tags: List<Any>,
    @SerializedName("images")
    val images: List<Any>,
    @SerializedName("isMembership")
    val isMembership: Boolean,
    @SerializedName("isHolding")
    val isHolding: Boolean,
    @SerializedName("remainingNumber")
    val remainingNumber: Int,
    @SerializedName("remainingDay")
    val remainingDay: Any,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("distance")
    val distance: Double
) {
    data class Seller(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: Any,
        @SerializedName("nickname")
        val nickname: String,
        @SerializedName("createdOn")
        val createdOn: String
    )
}