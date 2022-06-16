package com.depromeet.baton.domain.repository

import com.depromeet.baton.data.response.BookmarkTicket
import com.depromeet.baton.domain.api.ticket.BookmarkApi
import com.depromeet.baton.domain.api.user.UserInfoApi
import com.depromeet.baton.domain.di.IoDispatcher
import com.depromeet.baton.map.base.BaseApiResponse
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.remote.ticket.MypageBasicResponse
import com.depromeet.baton.remote.ticket.BookmarkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkRepository @Inject constructor(
    private val userInfoApi : UserInfoApi,
    private val bookmarkApi: BookmarkApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :BaseApiResponse(){

    suspend fun getUserBookmarks(userIdx : Int, state : Int ? =0) : NetworkResult<List<BookmarkTicket>> {
        return  withContext(ioDispatcher){safeApiCall { userInfoApi.getUserBookmarks(userIdx, state) }}
    }

    suspend fun deleteBookmark(bookmarkId : Int) : NetworkResult<MypageBasicResponse>{
        return withContext(ioDispatcher){safeApiCall { bookmarkApi.deleteBookmark(bookmarkId) }}
    }

    suspend fun postBookmark(userId: Int, ticketId :Int) :NetworkResult<BookmarkResponse>{
        return withContext(ioDispatcher){safeApiCall { bookmarkApi.postBookmark(userIdx = userId, ticketId = ticketId) }}
    }
}