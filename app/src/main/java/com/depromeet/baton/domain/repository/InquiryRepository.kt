package com.depromeet.baton.domain.repository

import com.depromeet.baton.data.request.PostInquiryRequest
import com.depromeet.baton.data.request.PostInquiryResponse
import com.depromeet.baton.data.response.ResponseGetInquiryByTicket
import com.depromeet.baton.domain.api.inquiry.InquiryApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InquiryRepository @Inject constructor(
    private val inquiryApi: InquiryApi
) {
    suspend fun postInquiry(request: PostInquiryRequest): Response<PostInquiryResponse> {
        return inquiryApi.postInquiry(request)
    }

    suspend fun getInquiryCount(ticketId: Int): Response<Int> {
        return inquiryApi.getInquiryCount(ticketId)
    }

    suspend fun getInquiryByTicket(ticketId: Int): Response<ResponseGetInquiryByTicket> {
        return inquiryApi.getInquiryByTicket(ticketId)
    }
}