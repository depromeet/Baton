package com.depromeet.baton.remote.search

import com.depromeet.baton.data.request.PostInquiryRequest
import com.depromeet.baton.data.request.PostInquiryResponse
import com.depromeet.baton.data.request.RequestPostFcm
import com.depromeet.baton.data.response.ResponseFilteredTicket
import com.depromeet.baton.data.response.ResponseGetInquiryByTicket
import com.depromeet.baton.data.response.ResponsePostTicket
import com.depromeet.baton.data.response.ResponseTicketInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface SearchService {

    //문의 요청 보내기
    @POST("inquiries")
    suspend fun postInquiry(
        @Body request: PostInquiryRequest
    ): Response<PostInquiryResponse>

    //받은 문의 개수 확인하기
    @GET("ticket/{ticket_id}/inquiries_count")
    suspend fun getInquiryCount(
        @Path("ticket_id") ticketId: Int,
    ): Response<Int>

    //티켓에 들어온 문의 요청 리스트
    @GET("ticket/{ticket_id}/inquiries")
    suspend fun getInquiryByTicket(
        @Path("ticket_id") ticketId: Int,
    ): Response<ResponseGetInquiryByTicket>

    //문의 요청 fcm 보내기
    @POST("fcm")
    suspend fun postFcm(
        @Body request: RequestPostFcm
    ): Response<String>

    @GET("ticket/count_query")
    suspend fun getFilteredTicketCount(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("place") place: String?,
        @Query("hashtag") hashtag: List<String>?,
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("town") town: String?,
        @Query("minPrice") minPrice: Int?,
        @Query("maxPrice") maxPrice: Int?,
        @Query("minRemainNumber") minRemainNumber: Int?,
        @Query("maxRemainNumber") maxRemainNumber: Int?,
        @Query("minRemainMonth") minRemainMonth: Int?,
        @Query("maxRemainMonth") maxRemainMonth: Int?,
        @Query("maxDistance") maxDistance: Int,
        @Query("ticketTypes") ticketTypes: List<String>?,
        @Query("ticketTradeType") ticketTradeType: String?,
        @Query("transferFee") transferFee: String?,
        @Query("ticketState") ticketState: String?,
        @Query("sortType") sortType: String?,
        @Query("hasClothes") hasClothes: Boolean?,
        @Query("hasLocker") hasLocker: Boolean?,
        @Query("hasShower") hasShower: Boolean?,
        @Query("hasGx") hasGx: Boolean?,
        @Query("canResell") canResell: Boolean?,
        @Query("canRefund") canRefund: Boolean?,
        @Query("isHold") isHold: Boolean?,
        @Query("canNego") canNego: Boolean?,
        @Query("isMembership") isMembership: Boolean?,
    ): Response<Int>

    @GET("ticket/query")
    suspend fun getFilteredTicket(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("place") place: String?,
        @Query("hashtag") hashtag: List<String>?,
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("town") town: String?,
        @Query("minPrice") minPrice: Int?,
        @Query("maxPrice") maxPrice: Int?,
        @Query("minRemainNumber") minRemainNumber: Int?,
        @Query("maxRemainNumber") maxRemainNumber: Int?,
        @Query("minRemainMonth") minRemainMonth: Int?,
        @Query("maxRemainMonth") maxRemainMonth: Int?,
        @Query("maxDistance") maxDistance: Int,
        @Query("ticketTypes") ticketTypes: List<String>?,
        @Query("ticketTradeType") ticketTradeType: String?,
        @Query("transferFee") transferFee: String?,
        @Query("ticketState") ticketState: String?,
        @Query("sortType") sortType: String?,
        @Query("hasClothes") hasClothes: Boolean?,
        @Query("hasLocker") hasLocker: Boolean?,
        @Query("hasShower") hasShower: Boolean?,
        @Query("hasGx") hasGx: Boolean?,
        @Query("canResell") canResell: Boolean?,
        @Query("canRefund") canRefund: Boolean?,
        @Query("isHold") isHold: Boolean?,
        @Query("canNego") canNego: Boolean?,
        @Query("isMembership") isMembership: Boolean?,
    ): Response<ResponseFilteredTicket>

    @GET("ticket/info/{ID}")
    suspend fun getTicketInfo(
        @Path("ID") id: Int,
        @Query("longitude") longitude: Float,
        @Query("latitude") latitude: Float,
    ): ResponseTicketInfo

    @DELETE("ticket/info/{ID}")
    suspend fun deleteTicketInfo(
        @Path("ID") id: Int,
    )

    @GET("ticket/string_query")
    suspend fun getTicketSearchResult(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("latitude") latitude: Float,
        @Query("longitude") longitude: Float,
        @Query("query") query: String,
        @Query("maxDistance") maxDistance: Int,
    ): Response<ResponseFilteredTicket>

    @Multipart
    @POST("ticket/post")
    suspend fun postTicket(
        @PartMap body: HashMap<String, RequestBody?>,
        @Part images: MutableList<MultipartBody.Part>?
    ): ResponsePostTicket

}