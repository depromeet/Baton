package com.depromeet.baton.domain.repository

import com.depromeet.baton.domain.model.AuthInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() {

    suspend fun setAuthInfo(authInfo: AuthInfo) {
        // 토큰을 safe 하게 디바이스에 저장하고 매번 불러써야한다.
        // 토큰을 접근할 때 만료되었다면? (어떻게 캐치함?) 리프레시 토큰으로 다시 토큰을 갱신해야한다.
    }
}
