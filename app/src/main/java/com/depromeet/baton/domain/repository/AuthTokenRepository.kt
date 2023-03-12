package com.depromeet.baton.domain.repository

import com.depromeet.baton.domain.api.user.TokenApi
import com.depromeet.baton.presentation.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthTokenRepository @Inject constructor(
    private val authRepository: AuthRepository,
    private val userinfoRepository: UserinfoRepository
) {
    private val _tokenError = SingleLiveEvent<Any>()
    val tokenError: SingleLiveEvent<Any> = _tokenError

    suspend fun authValidation(action : suspend  ()-> Unit){
        CoroutineScope(Dispatchers.Main).launch {
            userinfoRepository.authValidation(authRepository.authInfo?.accessToken!!,authRepository.authInfo?.refreshToken!!).let{
                when(it){
                    is TokenApi.RefreshResult.Success ->{
                        authRepository.setAuthInfo(it.response.access_token!!,it.response.refresh_token!!)
                        action()
                    }
                    is TokenApi.RefreshResult.Failure-> {
                        _tokenError.call()
                        authRepository.logout()
                    }
                }
            }
        }
    }
}