package com.depromeet.baton.domain.model

import com.depromeet.baton.R

enum class TicketStatus(val value: String) {
    SALE("판매중"), RESERVED("예약중"), SOLDOUT("판매완료")
}