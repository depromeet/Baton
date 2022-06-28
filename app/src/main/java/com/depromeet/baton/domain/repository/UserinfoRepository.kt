package com.depromeet.baton.domain.repository

import com.depromeet.baton.data.response.*
import com.depromeet.baton.domain.api.user.UserInfoApi
import com.depromeet.baton.domain.di.IoDispatcher
import com.depromeet.baton.domain.model.MypageTicketResponse
import com.depromeet.baton.map.base.BaseApiResponse
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.remote.ticket.MypageBasicResponse
import com.depromeet.baton.remote.user.UserProfileImg
import com.depromeet.baton.remote.user.UserProfileRequest
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Multipart
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserinfoRepository @Inject constructor(
    private val userInfoApi : UserInfoApi,
    @IoDispatcher val ioDispatcher: CoroutineDispatcher) :BaseApiResponse() {
    suspend fun getUserProfile(userIdx : Int) : NetworkResult<UserProfileResponse> {
       return withContext( ioDispatcher){safeApiCall { userInfoApi.getUserProfile(userIdx) }}
    }

    suspend fun updateUserProfile(userIdx: Int, nickname :String, phoneNum :String) :NetworkResult<UserProfileRequest>{
        return withContext(ioDispatcher){safeApiCall { userInfoApi.updateUserProfile( userIdx, UserProfileRequest(nickname, phoneNum)) }}
    }

    suspend fun getUserBookmarks(userIdx : Int, state : Int ? =0) :  NetworkResult<List<BookmarkTicket>>{
        return  withContext( ioDispatcher){safeApiCall { userInfoApi.getUserBookmarks(userIdx, state) }}
    }

    suspend fun getUserBuyList(userIdx: Int) :NetworkResult<List<UserBuyListResponse>>{
        return withContext( ioDispatcher){safeApiCall { userInfoApi.getUserBuyHistory(userIdx) }}
    }

    suspend fun getUserSellList(userIdx: Int ,state : Int ? =0) : NetworkResult<List<MypageTicketResponse>>{
        return  withContext( ioDispatcher){safeApiCall { userInfoApi.getUserSellHistory(userIdx,state) }}
    }

    suspend fun deleteUser(userIdx: Int) :NetworkResult<MypageBasicResponse>{
        return withContext(ioDispatcher){safeApiCall { userInfoApi.deleteUser(userIdx) }}
    }

    suspend fun updateProfileImage(userIdx: Int, url : MultipartBody.Part) :NetworkResult<UserProfileImg>{
        return withContext(ioDispatcher){safeApiCall { userInfoApi.updateProfileImage(userIdx, url) }}
    }

    suspend fun deleteProfileImage(userIdx: Int) :NetworkResult<MypageBasicResponse>{
        return withContext(ioDispatcher) {safeApiCall { userInfoApi.deleteProfileImage(userIdx) }}
    }

    private fun createPartFromString(stringData: String?): RequestBody? {
        return stringData?.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}