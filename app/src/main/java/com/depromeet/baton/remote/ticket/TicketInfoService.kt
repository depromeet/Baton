package com.depromeet.baton.remote.ticket

import com.depromeet.baton.domain.model.TicketInfo
import retrofit2.Response
import retrofit2.http.*

interface TicketInfoService {

    @GET("/search/ticket/info/{id}")
    suspend fun getTicketInfo(
        @Path("id") ticketId :Int,
        @Query("longitude") longitude : Float,
        @Query("latitude") latitude : Float
    ): Response<List<TicketInfo>>

    @DELETE("/search/ticket/info/{id}")
    suspend fun deleteTicket(
        @Path("id") ticketId :Int
    ): Response<String>
}