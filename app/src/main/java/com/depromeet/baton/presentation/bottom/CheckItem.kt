package com.depromeet.baton.presentation.bottom


data class CheckItem<T>(
    val data: T? = null,
    var isChecked: Boolean? = null
)