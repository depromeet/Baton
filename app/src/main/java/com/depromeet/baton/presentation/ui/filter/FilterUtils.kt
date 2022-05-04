package com.depromeet.baton.presentation.ui.filter

import com.depromeet.baton.R
import com.depromeet.baton.domain.model.TicketKind

internal fun getTicketKindFromCheckedId(checkedId: Int): TicketKind? {
    return when (checkedId) {
        R.id.chip_ticket_kind_gym -> TicketKind.GYM
        R.id.chip_ticket_kind_pt -> TicketKind.PT
        R.id.chip_ticket_kind_pilates_yoga -> TicketKind.PILATES_YOGA
        R.id.chip_ticket_kind_etc -> TicketKind.ETC
        else -> null
    }
}

