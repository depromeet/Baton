package com.depromeet.baton.remote.ticket

import androidx.annotation.Keep
import com.depromeet.baton.data.response.InquiryResponse

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InquiryService{
    @GET("inquiries/receive")
    suspend fun getRcvInquiryList() : Response<List<InquiryResponse>>

    @GET("inquiries/send")
    suspend fun getSendInquiryList() : Response<List<InquiryResponse>>

    @GET("inquiries/{id}")
    suspend fun getInquiryDetail(
        @Path("id") id : Int
    ) : Response<InquiryResponse>

    @POST("inquiries")
    suspend fun postInquiry(
        @Body inquiryRequest : InquiryRequest
    ):Response<*>
}

@Keep
data class InquiryRequest(
    val ticketId : Int,
    val content : String?=""
)