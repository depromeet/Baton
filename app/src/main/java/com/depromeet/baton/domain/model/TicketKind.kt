package com.depromeet.baton.domain.model

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
enum class TicketKind(val value: String)  {
    HEALTH("헬스 회원권"),
    PT("PT 이용권"),
    PILATES_YOGA("필라테스 회원권"),
    ETC("기타"),
}