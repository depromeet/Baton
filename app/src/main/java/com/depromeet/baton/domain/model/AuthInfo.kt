package com.depromeet.baton.domain.model

import androidx.annotation.Keep

@Keep
data class AuthInfo(
    var accessToken: String,
    var refreshToken: String,
    val userId: Int
)

