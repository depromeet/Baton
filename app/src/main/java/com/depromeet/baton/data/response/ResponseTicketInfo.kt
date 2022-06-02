package com.depromeet.baton.data.response


data class ResponseTicketInfo(
    val id: Int,
    val seller: Seller,
    val location: String,
    val address: String,
    val price: Int,
    val createAt: String,
    val expiryDate: String,
    val state: String,
    val type: String,
    val tradeType: String,
    val transferFee: String,
    val canNego: Boolean,
    val hasShower: Boolean,
    val hasLocker: Boolean,
    val hasClothes: Boolean,
    val hasGx: Boolean,
    val canResell: Boolean,
    val canRefund: Boolean,
    val description: String,
    val tags: List<String>,
    val images: List<Image>,
    val isMembership: Boolean,
    val isHolding: Boolean,
    val remainingNumber: Int,
    val latitude: Double,
    val longitude: Double,
    val distance: Double
) {
    data class Seller(
        val id: Int,
        val nickname: String,
        val gender: Boolean
    )

    data class Image(
        val id: Int,
        val url: String,
        val thumbnailUrl: String,
        val isMain: Boolean
    )
}