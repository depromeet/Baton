package com.depromeet.baton.domain.api.ticket

import com.depromeet.baton.remote.ticket.MypageBasicResponse
import com.depromeet.baton.remote.ticket.BookmarkRequest
import com.depromeet.baton.remote.ticket.BookmarkResponse
import com.depromeet.baton.remote.ticket.BookmarkService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkApi  @Inject constructor(
    private val service : BookmarkService
){

    suspend fun postBookmark( userIdx : Int , ticketId : Int):Response<BookmarkResponse>{
        return service.postBookmark(BookmarkRequest(userIdx,ticketId))
    }

    suspend fun deleteBookmark(bookmarkId: Int) : Response<MypageBasicResponse>{
        return service.deleteBookmark(bookmarkId)
    }
}