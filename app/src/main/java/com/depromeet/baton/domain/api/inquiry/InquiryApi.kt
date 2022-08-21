package com.depromeet.baton.domain.api.inquiry

import com.depromeet.baton.data.request.PostInquiryRequest
import com.depromeet.baton.data.request.PostInquiryResponse
import com.depromeet.baton.data.response.ResponseGetInquiryByTicket
import com.depromeet.baton.remote.inquiry.InquiryService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InquiryApi @Inject constructor(private val inquiryService: InquiryService) : InquiryService {
    override suspend fun postInquiry(request: PostInquiryRequest): Response<PostInquiryResponse> {
        return inquiryService.postInquiry(request)
    }

    override suspend fun getInquiryCount(ticketId: Int): Response<Int> {
        return inquiryService.getInquiryCount(ticketId)
    }

    override suspend fun getInquiryByTicket(ticketId: Int): Response<ResponseGetInquiryByTicket> {
        return inquiryService.getInquiryByTicket(ticketId)
    }
}

