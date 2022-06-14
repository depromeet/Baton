package com.depromeet.baton.domain.model

enum class Alignment(val value: String) {
    DISTANCE("가까운 거리 순"),
    LOWER_PRICE("낮은 가격 순"),
    VIEWS("인기 순"),
    REMAIN_DAY("남은 기간 많은 순"),
    REMAIN_NUMBER("남은 횟수 많은 순")
}