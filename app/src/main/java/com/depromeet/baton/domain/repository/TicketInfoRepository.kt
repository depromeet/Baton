package com.depromeet.baton.domain.repository

import com.depromeet.baton.domain.api.search.SearchApi
import com.depromeet.baton.domain.api.ticket.TicketInfoApi
import com.depromeet.baton.domain.di.IoDispatcher
import com.depromeet.baton.domain.model.TicketInfo
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.map.base.BaseApiResponse
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.remote.ticket.TicketQueryResponse
import com.depromeet.baton.util.MaxDistance
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketInfoRepository  @Inject constructor(
    private val ticketApi: TicketInfoApi,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher ) :BaseApiResponse(){

    suspend fun getTicketInfo(ticketId : Int, log :Float, lat :Float) : NetworkResult<TicketInfo>{
        return  withContext(ioDispatcher){
           safeApiCall { ticketApi.getTicketInfo(ticketId,log,lat) }
       }
    }

    suspend fun deleteTicket(ticketId : Int) : NetworkResult<String>{
        return  withContext(ioDispatcher){
           safeApiCall { ticketApi.deleteTicket(ticketId) }
        }
    }

    suspend fun getMoreTicket(size : Int,  log :Float, lat :Float, distance: Int) :NetworkResult<TicketQueryResponse>{
        return withContext(ioDispatcher){ safeApiCall { ticketApi.getMoreTicket(size,log,lat,distance) }}
    }
}