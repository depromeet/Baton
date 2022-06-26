package com.depromeet.baton.data.response

import androidx.annotation.Keep
import com.depromeet.baton.domain.model.MypageTicketResponse
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.squareup.moshi.Json

@Keep
data class UserBuyListResponse (
    @Json(name="id") val buyIdx : Int,
    @Json(name ="date") val date : String ?="",
    @Json(name="ticket") val ticket : MypageTicketResponse
    )