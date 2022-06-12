package com.depromeet.baton.domain.repository

import com.depromeet.baton.data.response.*
import com.depromeet.baton.domain.api.search.SearchApi
import com.depromeet.baton.domain.api.user.UserInfoApi
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.domain.model.UserInfo
import com.depromeet.baton.map.base.BaseApiResponse
import com.depromeet.baton.map.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserinfoRepository @Inject constructor(
    private val userInfoApi : UserInfoApi) :BaseApiResponse() {
    suspend fun getUserProfile(userIdx : Int) : NetworkResult<UserProfileResponse> {
       return withContext(Dispatchers.IO){safeApiCall { userInfoApi.getUserProfile(userIdx) }}
    }

    suspend fun getUserBookmarks(userIdx : Int, state : Int ? =0) :  NetworkResult<List<BookmarkTicket>>{
        return  withContext(Dispatchers.IO){safeApiCall { userInfoApi.getUserBookmarks(userIdx, state) }}
    }

    suspend fun getUserBuyList(userIdx: Int) :NetworkResult<List<UserBuyListResponse>>{
        return withContext(Dispatchers.IO){safeApiCall { userInfoApi.getUserBuyHistory(userIdx) }}
    }

    suspend fun getUserSellList(userIdx: Int ,state : Int ? =0) : NetworkResult<List<TicketSimpleInfo>>{
        return  withContext(Dispatchers.IO){safeApiCall { userInfoApi.getUserSellHistory(userIdx,state) }}
    }
}