package com.depromeet.baton.domain.api.ticket

import com.depromeet.baton.domain.model.TicketInfo
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.remote.ticket.TicketInfoService
import com.depromeet.baton.remote.ticket.TicketQueryResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketInfoApi @Inject constructor(
    private val service : TicketInfoService
){
   suspend fun deleteTicket(ticketId : Int) : Response<String> {
       return service.deleteTicket(ticketId)
   }

   suspend fun getTicketInfo(ticketId: Int, longitude : Float , latitude:Float) :Response<TicketInfo>{
       return service.getTicketInfo(ticketId ,longitude, latitude)
   }

   suspend fun getMoreTicket( size:Int, longitude : Float , latitude:Float, distance : Int) :Response<TicketQueryResponse>{
       return service.getMoreTicket(size=size, longitude = longitude, latitude = latitude, distance = distance)
   }
}