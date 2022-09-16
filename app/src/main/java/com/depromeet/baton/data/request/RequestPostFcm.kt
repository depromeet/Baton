package com.depromeet.baton.data.request

import androidx.annotation.Keep

@Keep
data class RequestPostFcm(
    val targetToken: String,
    val title: String,
    val body: String
)