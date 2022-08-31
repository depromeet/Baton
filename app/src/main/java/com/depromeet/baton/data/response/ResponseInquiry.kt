package com.depromeet.baton.data.response

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class InquiryResponse(
    @Json(name="id") val id : Int,
    @Json(name="user") val user : InquiryUserResponse,
    @Json(name="ticket") val ticketResponse: InquiryTicketResponse,
    @Json(name="content") val content : String,
    @Json(name="isRead") val isRead : Boolean?,
    @Json(name="createdAt") val createdAt :String?
)

@Keep
data class InquiryUserResponse (
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String?,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "image") val image : String,
    @Json(name = "phoneNumber") val phoneNumber: String?
)

@Keep
data class InquiryTicketResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "location") val location: String,
    @Json(name = "type") val type: String,
    @Json(name = "tradeType") val tradeType : String,
    @Json(name = "price") val price: Int,
    @Json(name = "canNego") val canNego: Boolean,
)