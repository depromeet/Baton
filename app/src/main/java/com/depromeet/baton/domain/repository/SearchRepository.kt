package com.depromeet.baton.domain.repository

import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.data.response.ResponseTicketInfo
import com.depromeet.baton.domain.api.search.SearchApi
import com.depromeet.baton.domain.api.search.UiState
import com.depromeet.baton.domain.model.BatonHashTag
import com.depromeet.baton.domain.model.RecentSearchKeyword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(private val searchApi: SearchApi) {
    private val ioDispatcher = Dispatchers.IO

    private val keywords: MutableStateFlow<List<RecentSearchKeyword>> =
        MutableStateFlow(emptyList())

    fun getRecentSearchKeywords(): Flow<List<RecentSearchKeyword>> {
        return keywords
    }

    suspend fun getRecommendHashTags(): List<BatonHashTag> {
        return HAST_TAGS
    }

    suspend fun addRecentSearchKeyword(keyword: RecentSearchKeyword) {
        withContext(ioDispatcher) {
            keywords.update { prev ->
                listOf(keyword) + prev.filter { it.title != keyword.title }
            }
        }
    }

    suspend fun removeRecentSearchKeyword(keyword: RecentSearchKeyword) {
        withContext(ioDispatcher) {
            keywords.update { prev ->
                prev.filter { it.title != keyword.title }
            }
        }
    }

    suspend fun clearAllRecentSearchKeywords() {
        withContext(ioDispatcher) {
            keywords.update { emptyList() }
        }
    }

    fun getFilteredTicketCount(
        page: Int,
        size: Int,
        place: String? = null,
        hashtag: List<String>? = null,
        latitude: Float ,
        longitude: Float ,
        town: String? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        minRemainNumber: Int? = null,
        maxRemainNumber: Int? = null,
        minRemainMonth: Int? = null,
        maxRemainMonth: Int? = null,
        maxDistance: Int ,
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
    ): Flow<UiState> {
        return searchApi.getFilteredTicketCount(
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

    suspend fun getFilteredTicket(
        page: Int,
        size: Int,
        place: String? = null,
        hashtag: List<String>? = null,
        latitude: Float ,
        longitude: Float ,
        town: String? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        minRemainNumber: Int? = null,
        maxRemainNumber: Int? = null,
        minRemainMonth: Int? = null,
        maxRemainMonth: Int? = null,
        maxDistance: Int ,
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
    ): List<ResponseFilteredTicket> {
        return searchApi.getFilteredTicket(
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

    suspend fun getTicketInfo(id: Int): ResponseTicketInfo {
        return searchApi.getTicketInfo(id)
    }

    suspend fun deleteTicketInfo(id: Int) {
        return searchApi.deleteTicketInfo(id)
    }

    suspend fun getTicketSearchResult(page: Int, size: Int, query: String): List<ResponseFilteredTicket> {
        return searchApi.getTicketSearchResult(page = page, size = size, query = query)
    }

    suspend fun postTicket(body: HashMap<String, RequestBody>, image: MultipartBody.Part?): ResponseFilteredTicket {
        return searchApi.postTicket(body, image)
    }

    companion object {
        private val HAST_TAGS = listOf(
            BatonHashTag("친절한 선생님"),
            BatonHashTag("체계적인 수업"),
            BatonHashTag("맞춤케어"),
            BatonHashTag("넓은 시설"),
            BatonHashTag("다양한 기구"),
            BatonHashTag("사람이 많은"),
            BatonHashTag("조용한 분위기"),
            BatonHashTag("역세권"),
            BatonHashTag("최신 기구"),
            BatonHashTag("쾌적한 환경"),
            BatonHashTag("사람이 적은"),
        )
    }
}

