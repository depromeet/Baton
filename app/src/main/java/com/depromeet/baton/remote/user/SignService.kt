package com.depromeet.baton.remote.user

import com.depromeet.baton.domain.model.LoginKakaoRequest
import com.depromeet.baton.domain.model.LoginKakaoResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface SignService {
    @POST("login/kakao")
    suspend fun loginKakao(@Body request: LoginKakaoRequest): LoginKakaoResponse
}
