package com.depromeet.baton.data.response

import androidx.annotation.Keep
import com.depromeet.baton.domain.model.MypageTicketResponse
import com.squareup.moshi.Json

@Keep
data class ResponseUserToken (
    @Json(name = "phone_number") val phone_number: String,
    @Json(name = "nickname") val nickname: String,
    @Json(name = "fcm_token") val fcm_token: String,
)