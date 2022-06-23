package com.depromeet.baton.presentation.ui.sign

data class Validation(
    val value: String = "",
    val errorReason: String? = null,
    val isValidating: Boolean = false
)
