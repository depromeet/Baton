package com.depromeet.baton.remote.inquiry

import com.depromeet.baton.data.request.PostInquiryRequest
import com.depromeet.baton.data.request.PostInquiryResponse
import com.depromeet.baton.data.response.ResponseGetInquiryByTicket
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface InquiryService {
    //문의 요청 보내기
    @POST("inquiries")
    suspend fun postInquiry(
        @Body request: PostInquiryRequest
    ): Response<PostInquiryResponse>

    //받은 문의 개수 확인하기
    @GET("ticket/{ticket_id}/inquiries_count")
    suspend fun getInquiryCount(
        @Path("ticket_id") ticketId: Int,
    ): Response<Int>

    //티켓에 들어온 문의 요청 리스트
    @GET("ticket/{ticket_id}/inquiries")
    suspend fun getInquiryByTicket(
        @Path("ticket_id") ticketId: Int,
    ): Response<ResponseGetInquiryByTicket>
}