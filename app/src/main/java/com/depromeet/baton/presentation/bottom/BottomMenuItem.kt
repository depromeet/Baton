package com.depromeet.baton.presentation.bottom


data class BottomMenuItem<T>(
    val listItem: T? = null,
    var isChecked: Boolean? = null
)