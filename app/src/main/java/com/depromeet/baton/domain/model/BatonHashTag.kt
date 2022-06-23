package com.depromeet.baton.domain.model

import androidx.annotation.Keep

@Keep
data class BatonHashTag(
    val title: String
) {

    val displayTitle: String
        get() = "#$title"
}
