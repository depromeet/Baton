package com.depromeet.baton.domain.repository

import com.depromeet.baton.data.remote.datasource.UiState
import kotlinx.coroutines.flow.Flow


interface TicketRepository {
    suspend fun getFilteredTicketCount(
        page: Int,
        size: Int,
        place: String? = null,
        hashtag: List<String>? = null,
        latitude: Float,
        longitude: Float,
        town: String? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        minRemainNumber: Int? = null,
        maxRemainNumber: Int? = null,
        minRemainMonth: Int? = null,
        maxRemainMonth: Int? = null,
        maxDistance: Int,
        ticketTypes: List<String>? = null,
        ticketTradeType: String? = null,
        transferFee: String? = null,
        ticketState: String? = null,
        sortType: String? = null,
        hasClothes: Boolean? = null,
        hasLocker: Boolean? = null,
        hasShower: Boolean? = null,
        hasGx: Boolean? = null,
        canResell: Boolean? = null,
        canRefund: Boolean? = null,
        isHold: Boolean? = null,
        canNego: Boolean? = null,
        isMembership: Boolean? = null,
    ): Flow<UiState>
}