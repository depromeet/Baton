package com.depromeet.baton.domain.model

import com.depromeet.baton.R

enum class TransferRightKind(val value: Int) {
    GYM(R.string.filter_transfer_right_kind_gym),
    PT(R.string.filter_transfer_right_kind_pt),
    PILATES_YOGA(R.string.filter_transfer_right_kind_pilates_yoga),
    ETC(R.string.filter_transfer_right_kind_etc),
}