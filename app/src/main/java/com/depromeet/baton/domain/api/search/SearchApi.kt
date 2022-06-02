package com.depromeet.baton.domain.api.search

import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.data.response.ResponseTicketInfo
import com.depromeet.baton.remote.search.SearchService
import com.depromeet.baton.util.BatonSpfManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchApi @Inject constructor(
    private val spfManager: BatonSpfManager,
    private val searchService: SearchService
) {

    fun getFilteredTicketCount(
        page: Int,
        size: Int,
        place: String?,
        hashtag: List<String>?,
        latitude: Float = spfManager.getLocation().latitude.toFloat(),
        longitude: Float = spfManager.getLocation().longitude.toFloat(),
        town: String?,
        minPrice: Int?,
        maxPrice: Int?,
        minRemainNumber: Int?,
        maxRemainNumber: Int?,
        minRemainMonth: Int?,
        maxRemainMonth: Int?,
        maxDistance: Int = spfManager.getMaxDistance().getDistance(),
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
    ): Flow<UiState> = flow<UiState>
    {
        val response = searchService.getFilteredTicketCount(
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
            emit(UiState.Error(Exception("[${response.code()}] - ${response.raw()}")))
        }

    }.flowOn(Dispatchers.IO)


    suspend fun getFilteredTicket(
        page: Int,
        size: Int,
        place: String?,
        hashtag: List<String>?,
        latitude: Float = spfManager.getLocation().latitude.toFloat(),
        longitude: Float = spfManager.getLocation().longitude.toFloat(),
        town: String?,
        minPrice: Int?,
        maxPrice: Int?,
        minRemainNumber: Int?,
        maxRemainNumber: Int?,
        minRemainMonth: Int?,
        maxRemainMonth: Int?,
        maxDistance: Int = spfManager.getMaxDistance().getDistance(),
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
    ): List<ResponseFilteredTicket> {
        return searchService.getFilteredTicket(
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

    suspend fun getTicketInfo(
        id: Int,
        longitude: Float = spfManager.getLocation().longitude.toFloat(),
        latitude: Float = spfManager.getLocation().latitude.toFloat()
    ): ResponseTicketInfo {
        return searchService.getTicketInfo(id, longitude, latitude)
    }

    suspend fun deleteTicketInfo(id: Int) {
        return searchService.deleteTicketInfo(id)
    }

    suspend fun getTicketSearchResult(
        page: Int,
        size: Int,
        latitude: Float = spfManager.getLocation().latitude.toFloat(),
        longitude: Float = spfManager.getLocation().longitude.toFloat(),
        query: String,
        maxDistance: Int = spfManager.getMaxDistance().getDistance(),
    ): List<ResponseFilteredTicket> {
        return searchService.getTicketSearchResult(page, size, latitude, longitude, query, maxDistance)
    }

    suspend fun postTicket(
        body: HashMap<String, RequestBody>,
        image: MultipartBody.Part?
    ): ResponseFilteredTicket {
        return searchService.postTicket(body, image)
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