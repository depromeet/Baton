package com.depromeet.baton.remote.user

import com.depromeet.baton.data.response.*
import com.depromeet.baton.domain.model.MypageTicketResponse
import com.depromeet.baton.domain.model.TicketSimpleInfo
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserInfoService {
    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") userIdx : Int) : Response<UserProfileResponse>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userIdx : Int)

    @GET("users/{id}/bookmarks")
    suspend fun getUserBookmarks(
        @Path("id") bookmarkId: Int,
        @Query("state")state :Int?=0

    ) : Response<List<BookmarkTicket>>

    @GET("users/{id}/buys")
    suspend fun getUserBuyTickets(@Path("id") userIdx : Int): Response<List<UserBuyListResponse>>

    @GET("users/{id}/sells")
    suspend fun getUserSellTickets(
        @Path("id") userIdx : Int,
        @Query("state") state :Int?=0
    ) : Response<List<MypageTicketResponse>>

}