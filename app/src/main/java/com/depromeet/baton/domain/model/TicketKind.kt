package com.depromeet.baton.domain.model

import com.depromeet.baton.R

enum class TicketKind(val value: Int) {
    GYM(R.string.filter_ticket_gym),
    PT(R.string.filter_ticket_pt),
    PILATES_YOGA(R.string.filter_ticket_pilates_yoga),
    ETC(R.string.filter_ticket_etc),
}