package com.depromeet.baton.domain.repository

import com.depromeet.baton.data.mapper.SearchMapper
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.util.BatonSpfManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetFilteredTicketUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val spfManager: BatonSpfManager
) {

    suspend fun execute(
        page: Int,
        size: Int,
        place: String? = null,
        hashtag: List<String>? = null,
        latitude: Float = spfManager.getLocation().latitude.toFloat(),
        longitude: Float = spfManager.getLocation().longitude.toFloat(),
        town: String? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        minRemainNumber: Int? = null,
        maxRemainNumber: Int? = null,
        minRemainMonth: Int? = null,
        maxRemainMonth: Int? = null,
        maxDistance: Int = spfManager.getMaxDistance().getDistance(),
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
        isMembership: Boolean? = null
    ): UIState {
        return SearchMapper.mapperToFilteredTicket(
            searchRepository.getFilteredTicket(
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
        )
    }
}
