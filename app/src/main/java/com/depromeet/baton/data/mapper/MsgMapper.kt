package com.depromeet.baton.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.depromeet.baton.data.response.InquiryResponse
import com.depromeet.baton.domain.model.Message
import com.depromeet.baton.domain.model.MsgType
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.presentation.util.getMsgDate

object MsgMapper {
    @RequiresApi(Build.VERSION_CODES.O)
    fun msgMapper(inquiryResponse: InquiryResponse, msgType: MsgType):Message{
        return Message(
            id =inquiryResponse.id,
            type = msgType,
            image = null,
            category =  when (inquiryResponse.ticketResponse.type) {
                TicketKind.PT.toString() -> "PT"
                TicketKind.HEALTH.toString() -> "헬스"
                TicketKind.PILATES_YOGA.toString() -> "필라테스"
                TicketKind.ETC.toString() -> "기타"
                else -> {
                    TicketKind.ETC.value
                }
            },
            gymName = inquiryResponse.ticketResponse.location,
            nickName = inquiryResponse.user.nickname,
            content = inquiryResponse.content,
            date= getMsgDate(inquiryResponse.createdAt?:""),
            isChecked = inquiryResponse.isRead,
        )
    }
}