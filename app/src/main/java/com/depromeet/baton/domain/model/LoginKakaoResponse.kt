package com.depromeet.baton.domain.model

import androidx.annotation.Keep
import com.squareup.moshi.Json

/**
 * kakao에서 발급받은 accessToken
 */
@Keep
data class LoginKakaoRequest(
    @Json(name = "access_token") val accessToken: String
)

/**
 * baton 서비스 accessToken
 */
@Keep
data class LoginKakaoResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String,
    @Json(name = "user") val user: UserResponse
)

@Keep
data class LoginKakaoNoSocialUserResponse(
    @Json(name = "uid") val uid: String,
    @Json(name = "nickname") val nickname: String?,
)
