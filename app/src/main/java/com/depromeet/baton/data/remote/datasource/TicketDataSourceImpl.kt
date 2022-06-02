package com.depromeet.baton.data.remote.datasource

import com.depromeet.baton.data.remote.api.ticket.TicketService
import com.depromeet.baton.data.remote.model.ResponseFilteredTicket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TicketDataSourceImpl @Inject constructor(
    private val ticketService: TicketService
) : TicketDataSource {
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
    ): Flow<UiState> = flow<UiState> {
        while (true) {
            val response = ticketService.getFilteredTicketCount(
                page,
                size,
                place,
                hashtag,
                latitude,
                longitude,
                town,
                minPrice,
                maxPrice,
                minRemainNumber,
                maxRemainNumber,
                minRemainMonth,
                maxRemainMonth,
                maxDistance,
                ticketTypes,
                ticketTradeType,
                transferFee,
                ticketState,
                sortType,
                hasClothes,
                hasLocker,
                hasShower,
                hasGx,
                canResell,
                canRefund,
                isHold,
                canNego,
                isMembership,
            )
            if (response.isSuccessful) {
                emit(UiState.Success(response.body()))
                delay(INTERVAL_REFRESH)
            } else {
                throw Exception("[${response.code()}] - ${response.raw()}")
            }
        }
    }.catch { UiState.Error(it) }
        .flowOn(Dispatchers.IO)


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
        return ticketService.getFilteredTicket(
            page,
            size,
            place,
            hashtag,
            latitude,
            longitude,
            town,
            minPrice,
            maxPrice,
            minRemainNumber,
            maxRemainNumber,
            minRemainMonth,
            maxRemainMonth,
            maxDistance,
            ticketTypes,
            ticketTradeType,
            transferFee,
            ticketState,
            sortType,
            hasClothes,
            hasLocker,
            hasShower,
            hasGx,
            canResell,
            canRefund,
            isHold,
            canNego,
            isMembership,
        )
    }

    companion object {
        private const val INTERVAL_REFRESH = 20000L
    }
}

sealed class UiState {
    object Loading : UiState()
    data class Success<T>(val data: T) : UiState()
    data class Error(val error: Throwable?) : UiState()
}