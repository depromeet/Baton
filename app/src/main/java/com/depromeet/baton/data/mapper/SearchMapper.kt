package com.depromeet.baton.data.mapper

import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.util.ceilAndToStringFormat
import com.depromeet.baton.presentation.util.priceFormat

object SearchMapper {
    fun mapperToFilteredTicket(uiState: UIState): UIState {
        return if (uiState is UIState.Success<*>) {
            val response = uiState.data as ResponseFilteredTicket
            UIState.Success(response.apply {
                this.mainImage
                this.location
                priceFormat(this.price?.toFloat())
                this.remainingNumber.toString()
                this.address
                ceilAndToStringFormat(500f) //this.distance!!.toFloat()
                this.tags
            })
        } else uiState
    }
}
