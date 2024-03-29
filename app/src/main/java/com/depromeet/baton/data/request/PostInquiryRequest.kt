package com.depromeet.baton.data.request

import androidx.annotation.Keep

@Keep
data class PostInquiryRequest(
    val userId :Int,
    val ticketId: Int,
    val content: String,
)