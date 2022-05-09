package com.depromeet.baton.domain.model

data class BatonHashTag(
    val title: String
) {

    val displayTitle: String
        get() = "#$title"
}
