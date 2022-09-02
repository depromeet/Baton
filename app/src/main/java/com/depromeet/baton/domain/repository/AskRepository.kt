package com.depromeet.baton.domain.repository

import com.depromeet.baton.domain.api.ticket.InquiriesApi
import com.depromeet.baton.domain.di.IoDispatcher
import com.depromeet.baton.map.base.BaseApiResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AskRepository @Inject constructor(
    private val InquiryApi : InquiriesApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): BaseApiResponse(){
   suspend fun getRcvMsgList() = withContext(ioDispatcher){ safeApiCall { InquiryApi.getRcvMsgList() }}
   suspend fun getSendMsgList() = withContext(ioDispatcher){ safeApiCall { InquiryApi.getSendMsgList()}}
   suspend fun getMsgDetail (id : Int) = withContext(ioDispatcher){safeApiCall { InquiryApi.getMsgDetail(id) }}
}