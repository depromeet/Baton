package com.depromeet.baton.domain.model

import androidx.annotation.Keep

@Keep
data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int
)

