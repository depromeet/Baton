package com.depromeet.baton.data.response

data class ResponseFilteredTicket(
    val id: Int?,
    val location: String?,
    val address: String?,
    val price: Int?,
    val mainImage: String?,
    val createAt: String?,
    val state: String?,
    val tags: List<String>?,
    val images: List<Image>?,
    val isMembership: Boolean?,
    val remainingNumber: Int?,
    val expiryDate: String?,
    val latitude: Double?,
    val longitude: Double?,
    val distance: Double?
) {
    data class Image(
        val id: Int?,
        val url: String?,
        val thumbnailUrl: String?,
        val isMain: Boolean?
    )
}
/*
package com.depromeet.baton.data.response

data class ResponseFilteredTicket(
    val id: Int,
    val location: String,
    val address: String,
    val price: Int,
    val mainImage: String,
    val createAt: String,
    val state: String,
    val tags: List<String>,
    val images: List<Image>,
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
*/
