package com.depromeet.baton.data.mapper

import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.domain.model.TicketKind
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.util.ceilAndToStringFormat
import com.depromeet.baton.presentation.util.priceFormat

object SearchMapper {
    fun mapperToFilteredTicket(uiState: UIState): UIState {
        if (uiState is UIState.Success<*>) {
            val response = uiState.data as ResponseFilteredTicket
            return UIState.Success(response.content?.map {
                FilteredTicket(
                    id = it.id,
                 //   bookmarkId = it.bookmarkId != null, todo
                    location = it.location,
                    address = it.address,
                    price = priceFormat(it.price.toFloat()),
                    mainImage = it.mainImage,
                    tags = it.tags,
                    remainingDay = it.remainingDay?.toString(),
                    remainingNumber = it.remainingNumber?.toString(),
                    latitude = it.latitude,
                    longitude = it.longitude,
                    distance = ceilAndToStringFormat((it.distance?.toFloat() ?: 0) as Float),
                    type = when (it.type) {
                        TicketKind.PT.toString() -> "PT"
                        TicketKind.HEALTH.toString() -> "헬스"
                        TicketKind.PILATES_YOGA.toString() -> "필라테스"
                        TicketKind.ETC.toString() -> "기타"
                        else -> {
                            TicketKind.ETC.value
                        }
                    }
                )
            })
        } else return uiState
    }
}
