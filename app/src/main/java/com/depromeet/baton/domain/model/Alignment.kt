package com.depromeet.baton.domain.model

enum class Alignment(val value: String) {
    RECENT("가까운 거리순"),
    LOWER_PRICE("낮은 가격순"),
    VIEW("인기순"),
    REMAIN_DAY("남은 기간 많은순")
}