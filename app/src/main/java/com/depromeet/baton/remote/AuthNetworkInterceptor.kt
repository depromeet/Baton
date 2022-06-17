package com.depromeet.baton.remote

import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.util.addHeaderIfPresent
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthNetworkInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeaderIfPresent("Authentication", authRepository.authInfo?.accessToken)
            .build()

        return chain.proceed(newRequest)
    }
}
