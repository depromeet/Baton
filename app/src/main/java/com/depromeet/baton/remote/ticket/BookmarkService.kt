package com.depromeet.baton.remote.ticket

import androidx.annotation.Keep
import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface  BookmarkService {
    @POST("user/bookmarks")
    suspend fun postBookmark(
        @Body request : BookmarkRequest
    ) : Response<BookmarkResponse>

    @DELETE("user/bookmarks/{id}")
    suspend fun deleteBookmark(
        @Path("id") bookmarkId : Int
    ) : Response<MypageBasicResponse>
}

@Keep
data class BookmarkRequest(
    @Json(name="user") val user : Int,
    @Json(name="ticket") val ticket : Int
)

@Keep
data class BookmarkResponse(
    @Json(name="id") val id : Int,
    @Json(name="user") val user : Int,
    @Json(name="ticket") val ticket : Int
)

@Keep
data class MypageBasicResponse(
    @Json(name="detail") val msg :String
)
