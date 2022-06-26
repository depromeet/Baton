package com.depromeet.baton.domain.model

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
enum class Term(val value: String) {
    PERIOD("기간제"),
    NUMBER("횟수제"),
}