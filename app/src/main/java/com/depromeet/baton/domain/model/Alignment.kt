package com.depromeet.baton.domain.model

enum class Alignment(val value: String) {
    DISTANCE("가까운 거리순"),
    LOW_PRICE("낮은 가격순"),
    HIGH_PRICE("낮은 가격순"),
    VIEW("조회 많은순"),
    BOOKMARK("북마크 많은순"),
    TERM("기간 많은순")
}