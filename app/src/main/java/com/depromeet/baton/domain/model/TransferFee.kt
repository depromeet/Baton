package com.depromeet.baton.domain.model

enum class TransferFee(val value: String) {
    SELLER("판매자 부담"),
    CONSUMER("구매자 부담"),
    NA("해당 없음")
}