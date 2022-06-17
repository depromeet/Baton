package com.depromeet.baton.domain.api.user

import com.depromeet.baton.domain.model.LoginKakaoNoSocialUserResponse
import com.depromeet.baton.domain.model.LoginKakaoRequest
import com.depromeet.baton.domain.model.LoginKakaoResponse
import com.depromeet.baton.domain.model.SignUpKakaoRequest
import com.depromeet.baton.remote.user.SignService
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignApi @Inject constructor(
    private val service: SignService,
    private val moshi: Moshi
) {

    private val loginKakaoNoSocialAdapter by lazy {
        moshi.adapter(LoginKakaoNoSocialUserResponse::class.java)
    }

    suspend fun signWithKakao(request: LoginKakaoRequest): KakaoLoginResult {
        return try {
            service.loginKakao(request).let { KakaoLoginResult.Success(it) }
        } catch (throwable: Throwable) {
            if (throwable is HttpException && throwable.code() == 401) {
                try {
                    val errorResponse = throwable.response()?.errorBody()
                        .also { Timber.d("beanbean error response > $it") }
                        ?.let { loginKakaoNoSocialAdapter.fromJson(it.string()) }
                    errorResponse
                        ?.let { KakaoLoginResult.NoSocialUser(it) }
                        ?: KakaoLoginResult.Failure(throwable)
                } catch (e: Throwable) {
                    KakaoLoginResult.Failure(e)
                }
            } else {
                KakaoLoginResult.Failure(throwable)
            }
        }
    }

    suspend fun signUpKakao(request: SignUpKakaoRequest): KakaoSignUpResult {
        return try {
            service.socialUsersKakao(request).let { KakaoSignUpResult.Success(it) }
        } catch (throwable: Throwable) {
            KakaoSignUpResult.Failure(throwable)
        }
    }

    sealed class KakaoLoginResult {
        data class Success(val response: LoginKakaoResponse) : KakaoLoginResult()
        data class NoSocialUser(val response: LoginKakaoNoSocialUserResponse) : KakaoLoginResult()
        data class Failure(val error: Throwable) : KakaoLoginResult()
    }

    sealed class KakaoSignUpResult {
        data class Success(val response: LoginKakaoResponse) : KakaoSignUpResult()
        data class Failure(val error: Throwable) : KakaoSignUpResult()
    }
}
