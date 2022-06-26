package com.depromeet.baton.domain.model

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
enum class FilterType(var value: String) {
    Alignment("정렬"),
    TicketKind("양도권 종류"),
    Term("기간"),
    Price("가격"),
    TransactionMethod("거래방법"),
    AdditionalOptions("추가옵션"),
    HashTag("해시태그"),
}