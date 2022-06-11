package com.depromeet.baton.data.mapper

import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.domain.model.FilteredTicket
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.presentation.util.ceilAndToStringFormat
import com.depromeet.baton.presentation.util.priceFormat

object SearchMapper {
    fun mapperToFilteredTicket(uiState: UIState): UIState {
        if (uiState is UIState.Success<*>) {
            val response = uiState.data as ResponseFilteredTicket
            return UIState.Success(response.content?.map {

                val category = if (it.location!!.contains("스포") ||
                    it.location.contains("스퀘어") ||
                    it.location.contains("휘트니스") ||
                    it.location.contains("플렉스") ||
                    it.location.contains("짐") ||
                    it.location.contains("PT")||
                    it.location.contains("헬스")
                ) {
                    "헬스"
                } else if (it.location.contains("필라테스") ||
                    it.location.contains("요가")
                ) {
                    "필라테스"
                } else "기타"

                FilteredTicket(
                    id = it.id!!,
                    location = it.location,
                    address = it.address,
                    price = priceFormat(it.price!!.toFloat()),
                    mainImage = it.mainImage,
                    tags = it.tags,
                    remainingDay = it.remainingDay?.toString(),
                    remainingNumber = it.remainingNumber?.toString(),
                    latitude = it.latitude,
                    longitude = it.longitude,
                    distance = ceilAndToStringFormat((it.distance?.toFloat() ?: 0) as Float),
                    category = category
                )
            })
        } else return uiState
    }
}
