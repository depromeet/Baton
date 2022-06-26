package com.depromeet.baton.domain.model

import androidx.annotation.Keep
import com.depromeet.baton.R
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
enum class TicketStatus(val value: String) {
    SALE("판매중"), RESERVED("예약중"), DONE("판매완료")
}