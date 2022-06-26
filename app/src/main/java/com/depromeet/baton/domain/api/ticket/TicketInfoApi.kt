package com.depromeet.baton.domain.api.ticket

import com.depromeet.baton.data.response.ResponseTicketInfo
import com.depromeet.baton.domain.model.TicketInfo
import com.depromeet.baton.remote.ticket.*
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

    suspend fun updateTicketState(ticketId: Int, state : String) : Response<ResponseTicketInfo>{
        return service.updateTicketState(ticketId, TicketStateRequest(state))
    }

    suspend fun reportTicket(ticketId: Int, content: String) : Response<String>{
        return service.reportTicket(TicketReportRequest(ticketId,content))
    }

    suspend fun reportUser( userId: Int, content: String) : Response<String>{
        return service.reportUser(UserReportRequest(userId,content))
    }
}