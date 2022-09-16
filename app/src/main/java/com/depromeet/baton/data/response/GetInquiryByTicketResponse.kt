package com.depromeet.baton.data.response

import androidx.annotation.Keep


@Keep
data class ResponseGetInquiryByTicket(
    val user: User,
    val ticket: Ticket,
    val content: String,
    val isRead: Boolean
) {
    data class User(
        val id: Int,
        val name: String,
        val nickname: String,
        val createdOn: String,
        val image: String
    )

    data class Ticket(
        val id: Int,
        val location: String,
        val type: String,
        val tradeType: String,
        val price: Int,
        val canNego: Boolean
    )
}
