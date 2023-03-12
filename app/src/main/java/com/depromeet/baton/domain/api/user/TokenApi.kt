package com.depromeet.baton.domain.api.user

import com.depromeet.baton.remote.user.TokenRefreshRequest
import com.depromeet.baton.remote.user.TokenService
import com.depromeet.baton.remote.user.TokenVerifyRequest
import com.depromeet.baton.data.response.ResponseAuthToken
import com.depromeet.baton.data.response.ResponseRefreshToken
import com.depromeet.baton.map.base.BaseApiResponse
import com.depromeet.baton.map.util.NetworkResult
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenApi @Inject constructor(
   val service: TokenService
) :BaseApiResponse(){
     suspend fun tokenValidation(accessToken : String , refreshToken: String) : RefreshResult {
         return try {
            safeApiCall { service.verifyToken(TokenVerifyRequest(accessToken)) }.let {
                Timber.e(it.message)
                when(it){
                    is NetworkResult.Success ->{
                        return RefreshResult.Success(ResponseRefreshToken(accessToken,refreshToken))
                    }
                    else ->{
                        if(it.code!=null && it.code==401){
                            refreshToken(refreshToken)
                        }
                        else return RefreshResult.Failure(Error("토큰 갱신 실패"))
                    }
                }
            }
         }catch (e : Throwable){
             return RefreshResult.Failure(e)
         }
    }

    private suspend fun refreshToken(refreshToken: String) : RefreshResult {
        return try {
            service.refreshToken(TokenRefreshRequest(refreshToken)).let{ RefreshResult.Success(it.body()!!)}
        } catch (e: Throwable) {
            Timber.e("refresh error"+ e.message)
            RefreshResult.Failure(e)
        }
    }

    sealed class RefreshResult {
        data class Success(val response: ResponseRefreshToken) :RefreshResult()
        data class Failure(val error: Throwable) :RefreshResult()
    }

    sealed class VerifyResult {
        data class Success(val response: ResponseAuthToken) : VerifyResult()
        data class Failure(val error: Throwable) : VerifyResult()
    }
}