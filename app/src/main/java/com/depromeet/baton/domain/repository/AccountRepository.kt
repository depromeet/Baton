package com.depromeet.baton.domain.repository

import com.depromeet.baton.domain.api.user.UserInfoApi
import com.depromeet.baton.map.base.BaseApiResponse
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.remote.ticket.MypageBasicResponse
import com.depromeet.baton.remote.user.UserAccount
import com.depromeet.baton.remote.user.UserAddressRequest
import com.depromeet.baton.remote.user.UserAddressResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(private val userInfoApi: UserInfoApi) :BaseApiResponse(){
    private val ioDispatcher = Dispatchers.IO

    suspend fun updateAddress(userIdx : Int, latitude :Float, longitude : Float, address:String, detail:String ): NetworkResult<UserAddressResponse> {
       return withContext(ioDispatcher){ safeApiCall { userInfoApi.updateUserAddress(userIdx, UserAddressRequest(latitude,longitude,address,detail))}}
    }

    suspend fun updateAccount(userIdx:Int,holder: String, bank:String, number: String ): NetworkResult<UserAccount>{
        return withContext(ioDispatcher){ safeApiCall { userInfoApi.updateUserAccount(userIdx,UserAccount( holder,bank,number)) }}
    }

    suspend fun deleteAccount(userIdx:Int ): NetworkResult<MypageBasicResponse>{
        return withContext(ioDispatcher){safeApiCall { userInfoApi.deleteUserAccount(userIdx)}}
    }

}