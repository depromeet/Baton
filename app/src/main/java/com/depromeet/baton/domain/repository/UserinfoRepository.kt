package com.depromeet.baton.domain.repository

import com.depromeet.baton.data.response.*
import com.depromeet.baton.domain.api.search.SearchApi
import com.depromeet.baton.domain.api.user.UserInfoApi
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.domain.model.UserInfo
import com.depromeet.baton.map.base.BaseApiResponse
import com.depromeet.baton.map.util.NetworkResult
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserinfoRepository @Inject constructor(
    private val userInfoApi : UserInfoApi) :BaseApiResponse() {
    suspend fun getUserProfile(userIdx : Int) : UserInfo {
        val response = safeApiCall { userInfoApi.getUserProfile(userIdx) }
        when (response) {
            is NetworkResult<UserProfileResponse> -> {
                if (response.data != null) {
                    val data = UserInfo(
                        userIdx = response.data!!.id,
                        name = response.data!!.name,
                        phoneNumber = response.data!!.phone_number,
                        joinDate = response.data!!.created_on,
                        account = UserInfo.Account()
                    )
                    return data
                }
                throw Error(response.message.toString())
            }
            else -> {
                throw Error(response.message.toString())
            }
        }
    }

    suspend fun getUserBookmarks(userIdx : Int, state : Int ? =0) :  NetworkResult<List<BookmarkTicket>>{
        return safeApiCall { userInfoApi.getUserBookmarks(userIdx, state) }
    }

    suspend fun getUserBuyList(userIdx: Int) :NetworkResult<List<UserBuyListResponse>>{
        return safeApiCall { userInfoApi.getUserBuyHistory(userIdx) }
    }

    suspend fun getUserSellList(userIdx: Int ,state : Int ? =0) : NetworkResult<List<TicketSimpleInfo>>{
        return safeApiCall { userInfoApi.getUserSellHistory(userIdx,state) }
    }
}