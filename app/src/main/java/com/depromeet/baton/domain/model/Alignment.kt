package com.depromeet.baton.domain.model

import com.depromeet.baton.R

enum class Alignment(val value: Int) {
    DISTANCE(R.string.filter_alignment_distance),
    LOW_PRICE(R.string.filter_alignment_low_price),
    HIGH_PRICE(R.string.filter_alignment_high_price),
    VIEW(R.string.filter_alignment_view),
    BOOKMARK(R.string.filter_alignment_bookmark),
    TERM(R.string.filter_alignment_term)
}