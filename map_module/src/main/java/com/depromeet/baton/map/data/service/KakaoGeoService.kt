package com.depromeet.baton.map.data


import com.depromeet.baton.map.data.model.KakaoGeoResponse
import com.depromeet.baton.map.data.service.KakaoApiClient
import com.depromeet.map.BuildConfig
import okhttp3.Interceptor
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query
import java.io.IOException

class  KakaoGeoService {
    private val CORD ="WGS84"
    val client = KakaoApiClient.instance?.create(KakaoGeoAPI::class.java)
    private val key = BuildConfig.KAKAO_REST_API_KEY
    suspend fun getAddressFromLocation(longitude : Double, latitude : Double)
            =client?.getAddress(longitude.toString(),latitude.toString(),CORD)
}

interface KakaoGeoAPI {
    @Headers("Authorization: KakaoAK " + BuildConfig.KAKAO_REST_API_KEY)
    @GET("/v2/local/geo/coord2address.json")
    suspend fun getAddress(
        @Query("x") xPos: String,
        @Query("y") yPos: String,
        @Query("input_coord") cord: String,
    ) :Response<KakaoGeoResponse>
}
