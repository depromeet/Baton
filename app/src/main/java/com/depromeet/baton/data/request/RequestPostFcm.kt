package com.depromeet.baton.data.request

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class RequestPostFcm(
    val userId : Int,
    val title: String,
    val body: String
)