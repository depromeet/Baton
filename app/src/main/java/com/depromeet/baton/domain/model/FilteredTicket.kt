package com.depromeet.baton.domain.model

data class FilteredTicket(
    val id: Int,
    val location: String?,
    val address: String?,
    val price: String,
    val mainImage: String?,
    val tags: List<String>?,
    val remainingDay: String?,
    val remainingNumber: String?,
    val latitude: Double,
    val longitude: Double,
    val distance: String?,
    val type: String?,
 //   val bookmarkId: Boolean? todo
)
