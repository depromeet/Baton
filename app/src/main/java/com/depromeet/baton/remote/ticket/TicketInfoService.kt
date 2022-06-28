package com.depromeet.baton.remote.ticket

import androidx.annotation.Keep
import com.depromeet.baton.data.response.ResponseTicketInfo
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


    @PUT("/search/ticket/info/{id}")
    suspend fun updateTicketState(
        @Path("id") ticketId :Int,
        @Body ticketState: TicketStateRequest
    ): Response<ResponseTicketInfo>

    @POST("/search/report/ticket")
    suspend fun reportTicket(
        @Body body : TicketReportRequest
    ) : Response<String>

    @POST("/search/report/user")
    suspend fun reportUser(
        @Body body : UserReportRequest
    ) : Response<String>

}

@Keep
data class TicketQueryResponse(
    @Json(name="content")val content : List<TicketSimpleInfo>
)

@Keep
data class TicketStateRequest(
    val ticketState: String
)

@Keep
data class TicketReportRequest(
    @Json(name="ticketId") val ticketId : Int,
    @Json(name="content") val content : String
)

@Keep
data class UserReportRequest(
    @Json(name="userId") val userId : Int,
    @Json(name="content") val content : String
)