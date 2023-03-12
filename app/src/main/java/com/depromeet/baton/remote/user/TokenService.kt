package com.depromeet.baton.remote.user

import androidx.annotation.Keep
import com.depromeet.baton.data.response.ResponseAuthToken
import com.depromeet.baton.data.response.ResponseRefreshToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TokenService {
    @POST("token/verify")
    suspend fun verifyToken(@Body request: TokenVerifyRequest)
    : Response<ResponseAuthToken>

    @POST("token/refresh")
    suspend fun refreshToken(@Body request: TokenRefreshRequest)
    : Response<ResponseRefreshToken>
}

@Keep
data class TokenVerifyRequest(
    val token : String
)

@Keep
data class TokenRefreshRequest(
    val refresh_token : String
)