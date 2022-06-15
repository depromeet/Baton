package com.depromeet.baton.domain.api.user

import com.depromeet.baton.data.response.BookmarkTicket
import com.depromeet.baton.data.response.UserBuyListResponse
import com.depromeet.baton.data.response.UserProfileResponse
import com.depromeet.baton.databinding.ItemPrimaryOutlineTagBinding
import com.depromeet.baton.domain.model.MypageTicketResponse
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.map.base.BaseApiResponse
import com.depromeet.baton.remote.user.*
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoApi @Inject constructor(
    private val service : UserInfoService
){
    suspend fun getUserProfile(userIdx : Int) : Response<UserProfileResponse> {
       return service.getUserProfile(userIdx)
    }
    suspend fun updateUserProfile(userIdx : Int , body : UserProfileRequest) : Response<UserProfileRequest> {
        return service.updateUserProfile(userIdx , body)
    }

      suspend fun getUserBookmarks(bookmarkId : Int , state: Int?=0) :Response<List<BookmarkTicket>>{
        return service.getUserBookmarks(bookmarkId, state)
    }

    suspend fun getUserBuyHistory(userIdx: Int) : Response<List<UserBuyListResponse>>{
        return service.getUserBuyTickets(userIdx)
    }

    suspend fun getUserSellHistory(userIdx: Int, state: Int?) : Response<List<MypageTicketResponse>>{
        return service.getUserSellTickets(userIdx,state)
    }


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