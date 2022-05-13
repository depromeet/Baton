package com.depromeet.baton.presentation.ui.detail.model

import com.depromeet.baton.domain.model.HashTag
import com.depromeet.baton.domain.model.TicketKind

data class TicketInfo (
    val ticketKind : TicketKind,
    val effectiveDate : String,
    val price : String,
    val transferCoast :String,
    val infoTag : ArrayList<HashTag>
)

