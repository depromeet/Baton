package com.depromeet.baton.domain.model

import com.squareup.moshi.Json

/**
 * kakao에서 발급받은 accessToken
 */
data class LoginKakaoRequest(
    @Json(name = "access_token") val accessToken: String
)

/**
 * baton 서비스 accessToken
 */
data class LoginKakaoResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "user") val user: UserResponse
)

data class LoginKakaoNoSocialUserResponse(
    @Json(name = "uid") val uid: String,
    @Json(name = "nickname") val nickname: String,
)
