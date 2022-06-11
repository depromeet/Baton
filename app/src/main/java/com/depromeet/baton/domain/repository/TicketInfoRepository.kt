package com.depromeet.baton.domain.repository

import com.depromeet.baton.domain.api.search.SearchApi
import com.depromeet.baton.domain.api.ticket.TicketInfoApi
import com.depromeet.baton.domain.model.TicketInfo
import com.depromeet.baton.map.base.BaseApiResponse
import com.depromeet.baton.map.util.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketInfoRepository  @Inject constructor(private val ticketApi: TicketInfoApi) :BaseApiResponse(){
    suspend fun getTicketInfo(ticketId : Int, log :Float, lat :Float) : NetworkResult<List<TicketInfo>>{
        return  withContext(Dispatchers.IO){
           return@withContext safeApiCall { ticketApi.getTicketInfo(ticketId,log,lat) }
       }
    }


    suspend fun deleteTicket(ticketId : Int) : NetworkResult<String>{
        return  withContext(Dispatchers.IO){
            return@withContext safeApiCall { ticketApi.deleteTicket(ticketId) }
        }
    }
}