package com.depromeet.baton.domain.model

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
enum class TransferFee(val value: String) {
    SELLER("판매자 부담"),
    CONSUMER("구매자 부담"),
    NONE("해당 없음")
}