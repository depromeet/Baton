package com.depromeet.baton.presentation.bottom


data class MenuItem<T>(
    val listItem: T? = null,
    var isChecked: Boolean? = null
)