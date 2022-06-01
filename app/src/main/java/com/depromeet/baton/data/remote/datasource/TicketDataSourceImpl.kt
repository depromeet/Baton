package com.depromeet.baton.data.remote.datasource

import com.depromeet.baton.data.remote.api.ticket.TicketService
import com.depromeet.baton.data.remote.model.ResponseFilteredTicket
import javax.inject.Inject

class TicketDataSourceImpl @Inject constructor(
    private val ticketService: TicketService
) : TicketDataSource {

    override suspend fun getFilteredTicket(
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
        isMembership: Boolean?
    ): ResponseFilteredTicket {
        return ticketService.getFilteredTicket(
            page = page,
            size = size,
            place = place,
            hashtag = hashtag,
            latitude = latitude,
            longitude = longitude,
            town = town,
            minPrice = minPrice,
            maxPrice = maxPrice,
            minRemainNumber = minRemainNumber,
            maxRemainNumber = maxRemainNumber,
            minRemainMonth = minRemainMonth,
            maxRemainMonth = maxRemainMonth,
            maxDistance = maxDistance,
            ticketTypes = ticketTypes,
            ticketTradeType = ticketTradeType,
            transferFee = transferFee,
            ticketState = ticketState,
            sortType = sortType,
            hasClothes = hasClothes,
            hasLocker = hasLocker,
            hasShower = hasShower,
            hasGx = hasGx,
            canResell = canResell,
            canRefund = canRefund,
            isHold = isHold,
            canNego = canNego,
            isMembership = isMembership
        )
    }
}