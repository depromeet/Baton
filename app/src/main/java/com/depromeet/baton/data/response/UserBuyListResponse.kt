package com.depromeet.baton.data.response

import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.squareup.moshi.Json

data class UserBuyListResponse (
    @Json(name="id") val buyIdx : Int,
    @Json(name ="date") val date : String ?="",
    @Json(name="ticket") val ticket : TicketSimpleInfo
)