package com.depromeet.baton.data.remote.datasource

import com.depromeet.baton.data.remote.model.ResponseFilteredTicket

interface TicketDataSource {
    suspend fun getFilteredTicket(
        page: Int,
        size: Int,
        place: String?,
        hashtag: MutableList<String>?,
        latitude: Float,
        longitude: Float,
        town: String?,
        minPrice: Int?,
        maxPrice: Int?,
        minRemainNumber: Int?,
        maxRemainNumber: Int?,
        minRemainMonth: Int?,
        maxRemainMonth: Int?,
        maxDistance: Int,
        ticketTypes: MutableList<String>?,
        ticketTradeType: String?,
        transferFee: String?,
        ticketState: String?,
        sortType: String?,
        hasClothes: Boolean?,
        hasLocker: Boolean?,
        hasShower: Boolean?,
        hasGx: Boolean?,
        canResell: Boolean?,
        canRefund: Boolean?,
        isHold: Boolean?,
        canNego: Boolean?,
        isMembership: Boolean?,
    ): ResponseFilteredTicket
}