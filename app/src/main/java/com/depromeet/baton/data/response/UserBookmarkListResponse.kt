package com.depromeet.baton.data.response

import androidx.annotation.Keep
import com.depromeet.baton.domain.model.MypageTicketResponse
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.squareup.moshi.Json

@Keep
data class BookmarkTicket(
    @Json(name = "id")  val id : Int,
    @Json(name = "ticket") val ticket :MypageTicketResponse
)