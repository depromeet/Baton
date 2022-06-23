package com.depromeet.baton.domain.model

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
enum class AdditionalOptions(val value: String) {
    SHOWER_ROOM("샤워실 포함"),
    LOCKER_ROOM("락카룸 포함"),
    SPORT_WEAR("운동복 포함"),
    GX("GX 포함"),
    RE_TRANSFER("재양도 가능"),
    REFUND("환불 가능"),
    HOLDING("홀딩(휴회)완료"),
    BARGAINING("가격 제안 가능"),
}