package com.depromeet.baton.domain.model

enum class TradeType(val value: String) {
    CONTECT("현장결제(대면)"),
    UNTECT("선결제(비대면)"),
    BOTH("현장결제/선결제 둘 다 가능")
}
