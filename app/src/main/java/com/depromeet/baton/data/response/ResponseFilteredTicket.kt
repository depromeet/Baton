package com.depromeet.baton.data.response


import com.google.gson.annotations.SerializedName

data class ResponseFilteredTicket(
    val content: List<Content>?,
    val pageable: Pageable?,
    val totalElements: Int?,
    val totalPages: Int?,
    val last: Boolean?,
    val size: Int?,
    val number: Int?,
    val sort: Sort?,
    val numberOfElements: Int?,
    val first: Boolean?,
    val empty: Boolean?
) {
    data class Content(
        val createAt: String?,
        val expiryDate: String?,
        val id: Int?,
        val location: String?,
        val address: String?,
        val price: Int?,
        val mainImage: String?,
        val tags: List<String>?,
        val isMembership: Boolean?,
        val remainingNumber: Int?,
        val remainingDay: Int?,
        val state: String?,
        val latitude: Double?,
        val longitude: Double?,
        val distance: Double?
    )

    data class Pageable(
        val sort: Sort?,
        val offset: Int?,
        val pageNumber: Int?,
        val pageSize: Int?,
        val paged: Boolean?,
        val unpaged: Boolean?
    ) {
        data class Sort(
            val empty: Boolean?,
            val sorted: Boolean?,
            val unsorted: Boolean?
        )
    }

    data class Sort(
        val empty: Boolean?,
        val sorted: Boolean?,
        val unsorted: Boolean?
    )
}