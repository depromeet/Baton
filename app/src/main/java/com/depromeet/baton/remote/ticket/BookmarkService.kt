package com.depromeet.baton.remote.ticket

import com.depromeet.baton.domain.model.LoginKakaoRequest
import com.depromeet.baton.domain.model.LoginKakaoResponse
import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path
import java.net.IDN

interface  BookmarkService {
    @POST("bookmarks")
    suspend fun postBookmark(
        @Body request : BookmarkRequest
    ) : Response<BookmarkResponse>

    @DELETE("bookmarks/{id}")
    suspend fun deleteBookmark(
        @Path("id") bookmarkId : Int
    ) : Response<BookmarkDeleteResponse>
}

data class BookmarkRequest(
    @Json(name="user") val user : Int,
    @Json(name="ticket") val ticket : Int
)

data class BookmarkResponse(
    @Json(name="user") val user : Int,
    @Json(name="ticket") val ticket : Int
)

data class BookmarkDeleteResponse(
    @Json(name="detail") val msg :String
)