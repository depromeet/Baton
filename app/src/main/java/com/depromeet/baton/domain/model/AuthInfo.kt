package com.depromeet.baton.domain.model

data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int
)

