package com.depromeet.baton.data.repository

import com.depromeet.baton.data.remote.datasource.TicketDataSource
import com.depromeet.baton.data.remote.datasource.UiState
import com.depromeet.baton.data.remote.model.ResponseFilteredTicket
import com.depromeet.baton.domain.repository.TicketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class TicketRepositoryImpl @Inject constructor(
    private val ticketDataSource: TicketDataSource
) : TicketRepository {

    override suspend fun getFilteredTicketCount(
        page: Int,
        size: Int,
        place: String?,
        hashtag: List<String>?,
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
        ticketTypes: List<String>?,
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
    ): Flow<UiState> {
        return ticketDataSource.getFilteredTicketCount(
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

    override suspend fun getFilteredTicket(
        page: Int,
        size: Int,
        place: String?,
        hashtag: List<String>?,
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
        ticketTypes: List<String>?,
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
    ):  List<ResponseFilteredTicket> {
        return ticketDataSource.getFilteredTicket(
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