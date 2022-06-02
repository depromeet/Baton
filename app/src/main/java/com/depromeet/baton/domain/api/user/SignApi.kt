package com.depromeet.baton.domain.api.user

import com.depromeet.baton.domain.model.LoginKakaoRequest
import com.depromeet.baton.domain.model.LoginKakaoResponse
import com.depromeet.baton.remote.user.SignService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignApi @Inject constructor(
    private val service: SignService
) {

    suspend fun signWithKakao(request: LoginKakaoRequest): LoginKakaoResponse {
        return service.loginKakao(request)
    }
}
