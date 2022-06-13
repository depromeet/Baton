package com.depromeet.baton.domain.api.user

import com.depromeet.baton.remote.user.UserAccount
import com.depromeet.baton.remote.user.UserAddressRequest
import com.depromeet.baton.remote.user.UserAddressResponse
import com.depromeet.baton.remote.user.UserInfoService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserInfoApi  @Inject constructor(
    private val service: UserInfoService
){
    suspend fun updateUserAddress(userIdx : Int, request: UserAddressRequest) : Response<UserAddressResponse> {
        return service.updateUserAddress(userIdx, request)
    }

    suspend fun getUserAccount(userIdx : Int) : Response<UserAccount> {
        return service.getUserAccount(userIdx)
    }
    suspend fun updateUserAccount(userIdx : Int, request: UserAccount) : Response<UserAccount> {
        return service.updateUserAccount(userIdx, request)
    }

    suspend fun postUserAccount(userIdx : Int, request: UserAccount) : Response<UserAccount> {
        return service.postUserAccount(userIdx, request)
    }


}