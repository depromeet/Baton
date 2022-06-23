package com.depromeet.baton.domain.model

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
enum class TradeType(val value: String) {
    CONTECT("현장결제(대면)"),
    UNTECT("선결제(비대면)"),
    BOTH("둘다가능"), //바텀 필터에서 사용됨
    BOTH_DETAIL("현장결제/선결제 둘 다 가능")
}
