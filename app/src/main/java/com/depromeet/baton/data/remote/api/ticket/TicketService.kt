package com.depromeet.baton.data.remote.api.ticket

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TicketService {

    @GET("ticket/count_query")
    suspend fun getFilteredTicketCount(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("place") place: String? = null,
        @Query("hashtag") hashtag: List<String>? = null,
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("town") town: String? = null,
        @Query("minPrice") minPrice: Int? = null,
        @Query("maxPrice") maxPrice: Int? = null,
        @Query("minRemainNumber") minRemainNumber: Int? = null,
        @Query("maxRemainNumber") maxRemainNumber: Int? = null,
        @Query("minRemainMonth") minRemainMonth: Int? = null,
        @Query("maxRemainMonth") maxRemainMonth: Int? = null,
        @Query("maxDistance") maxDistance: Int,
        @Query("ticketTypes") ticketTypes: List<String>? = null,
        @Query("ticketTradeType") ticketTradeType: String? = null,
        @Query("transferFee") transferFee: String? = null,
        @Query("ticketState") ticketState: String? = null,
        @Query("sortType") sortType: String? = null,
        @Query("hasClothes") hasClothes: Boolean? = null,
        @Query("hasLocker") hasLocker: Boolean? = null,
        @Query("hasShower") hasShower: Boolean? = null,
        @Query("hasGx") hasGx: Boolean? = null,
        @Query("canResell") canResell: Boolean? = null,
        @Query("canRefund") canRefund: Boolean? = null,
        @Query("isHold") isHold: Boolean? = null,
        @Query("canNego") canNego: Boolean? = null,
        @Query("isMembership") isMembership: Boolean? = null,
    ): Response<Int>
}