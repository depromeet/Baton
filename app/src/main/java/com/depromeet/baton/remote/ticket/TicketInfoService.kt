package com.depromeet.baton.remote.ticket

import com.depromeet.baton.domain.model.TicketInfo
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.*

interface TicketInfoService {

    @GET("/search/ticket/info/{id}")
    suspend fun getTicketInfo(
        @Path("id") ticketId :Int,
        @Query("longitude") longitude : Float,
        @Query("latitude") latitude : Float
    ): Response<TicketInfo>

    @DELETE("/search/ticket/info/{id}")
    suspend fun deleteTicket(
        @Path("id") ticketId :Int
    ): Response<String>


    @GET("/search/ticket/query")
    suspend fun getMoreTicket(
        @Query("page") page:Int?=0,
        @Query("size") size:Int,
        @Query("sortType") sortType: String? = "RECENT",
        @Query("longitude") longitude : Float,
        @Query("latitude") latitude : Float,
        @Query("maxDistance") distance:Int,
    ): Response<TicketQueryResponse>
}

data class TicketQueryResponse(
    @Json(name="content")val content : List<TicketSimpleInfo>
)