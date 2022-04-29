package com.depromeet.baton.presentation.ui.filter

import com.depromeet.baton.R
import com.depromeet.baton.domain.model.FilterType
import com.depromeet.baton.domain.model.TicketKind

fun getTicketKindFromCheckedId(checkedId: Int): TicketKind? {
    return when (checkedId) {
        R.id.chip_ticket_kind_gym -> TicketKind.GYM
        R.id.chip_ticket_kind_pt -> TicketKind.PT
        R.id.chip_ticket_kind_pilates_yoga -> TicketKind.PILATES_YOGA
        R.id.chip_ticket_kind_etc -> TicketKind.ETC
        else -> null
    }
}

fun getFilterTypeFromPosition(position: Int): FilterType? {
    return when (position) {
        0 -> FilterType.Alignment
        1 -> FilterType.TicketKind
        2 -> FilterType.Term
        3 -> FilterType.Price
        4 -> FilterType.TransactionMethod
        5 -> FilterType.AdditionalOptions
        6 -> FilterType.HashTag
        else -> null
    }
}

fun getPositionFromFilterType(filterType: FilterType): Int {
    return when (filterType) {
        FilterType.Alignment -> 0
        FilterType.TicketKind -> 1
        FilterType.Term -> 2
        FilterType.Price -> 3
        FilterType.TransactionMethod -> 4
        FilterType.AdditionalOptions -> 5
        FilterType.HashTag -> 6
    }
}