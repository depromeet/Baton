package com.depromeet.baton.map.data.service


import com.depromeet.baton.map.data.model.KakaoGeoResponse
import com.depromeet.map.BuildConfig
import retrofit2.Response

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

class  KakaoGeoService {
    private val CORD ="WGS84"
    val client = KakaoApiClient.instance?.create(KakaoGeoAPI::class.java)
    private val key = BuildConfig.KAKAO_REST_API_KEY
    suspend fun getAddressFromLocation(longitude : Double, latitude : Double)
            =client?.getAddress(longitude.toString(),latitude.toString(),CORD)
}

interface KakaoGeoAPI {

    @Headers("Authorization: KakaoAK " + key)
    @GET("/v2/local/geo/coord2address.json")
    suspend fun getAddress(
        @Query("x") xPos: String,
        @Query("y") yPos: String,
        @Query("input_coord") cord: String,
    ) :Response<KakaoGeoResponse>

    companion object{
       private  const val key  = BuildConfig.KAKAO_REST_API_KEY
    }
}
