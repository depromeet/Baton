package com.depromeet.baton.domain.api.ticket

import com.depromeet.baton.domain.di.IoDispatcher
import com.depromeet.baton.map.base.BaseApiResponse
import com.depromeet.baton.remote.ticket.InquiryRequest
import com.depromeet.baton.remote.ticket.InquiryService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InquiriesApi  @Inject constructor(
    private val service : InquiryService,
): BaseApiResponse(){
    suspend fun getRcvMsgList() = service.getRcvInquiryList()
    suspend fun getSendMsgList() =  service.getSendInquiryList()
    suspend fun getMsgDetail (id : Int) = service.getInquiryDetail(id)
    suspend fun postInquiry (body : InquiryRequest) = service.postInquiry(body)
}