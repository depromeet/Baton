package com.depromeet.baton.domain.model

import com.depromeet.baton.R

enum class TransactionMethod(val value: Int) {
    FACE(R.string.filter_transaction_method_select_face),
    NON_FACE(R.string.filter_transaction_method_select_non_face),
    SELLER(R.string.filter_transaction_method_cost_select_seller),
    CONSUMER(R.string.filter_transaction_method_cost_select_consumer),
}