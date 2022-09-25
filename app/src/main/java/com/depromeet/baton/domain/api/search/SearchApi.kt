package com.depromeet.baton.domain.api.search

import com.depromeet.baton.data.request.PostInquiryRequest
import com.depromeet.baton.data.request.PostInquiryResponse
import com.depromeet.baton.data.request.RequestPostFcm
import com.depromeet.baton.data.response.ResponseGetInquiryByTicket
import com.depromeet.baton.data.response.ResponsePostTicket
import com.depromeet.baton.data.response.ResponseTicketInfo
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.remote.search.SearchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchApi @Inject constructor(private val searchService: SearchService) {
    suspend fun postInquiry(request: PostInquiryRequest): Response<PostInquiryResponse> {
        return searchService.postInquiry(request)
    }

    suspend fun getInquiryCount(ticketId: Int): Response<Int> {
        return searchService.getInquiryCount(ticketId)
    }

    suspend fun getInquiryByTicket(ticketId: Int): Response<List<ResponseGetInquiryByTicket>> {
        return searchService.getInquiryByTicket(ticketId)
    }

    suspend fun postFcm(request: RequestPostFcm) {
        return searchService.postFcm(request)
    }

    fun getFilteredTicketCount(
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
    ): Flow<UIState> = flow<UIState>
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
            emit(UIState.Success(response.body()))
            //  delay(INTERVAL_REFRESH)
        } else {
            emit(UIState.Error("[${response.code()}] - ${response.raw()}"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getFilteredTicket(
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
    ): UIState {
        val response = searchService.getFilteredTicket(
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
        return if (response.isSuccessful) {
            UIState.Success(response.body())
        } else UIState.Error("[${response.code()}] - ${response.raw()}")
    }

    suspend fun getTicketInfo(
        id: Int, longitude: Float, latitude: Float
    ): ResponseTicketInfo {
        return searchService.getTicketInfo(id, longitude, latitude)
    }

    suspend fun deleteTicketInfo(id: Int) {
        return searchService.deleteTicketInfo(id)
    }

    suspend fun getTicketSearchResult(
        page: Int,
        size: Int,
        latitude: Float,
        longitude: Float,
        query: String,
        maxDistance: Int,
    ): UIState {
        val response = searchService.getTicketSearchResult(page, size, latitude, longitude, query, maxDistance)
        return if (response.isSuccessful) {
            UIState.Success(response.body())
        } else UIState.Error("[${response.code()}] - ${response.raw()}")
    }

    suspend fun postTicket(
        body: HashMap<String, RequestBody?>,
        images: MutableList<MultipartBody.Part>?
    ): ResponsePostTicket {
        return searchService.postTicket(body, images)
    }

    companion object {
        private const val INTERVAL_REFRESH = 20000L
    }
}
