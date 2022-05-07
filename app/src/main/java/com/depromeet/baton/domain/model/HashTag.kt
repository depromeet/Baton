package com.depromeet.baton.domain.model

data class HashTag(
    val title: String
) {

    val displayTitle: String
        get() = "#$title"
}
