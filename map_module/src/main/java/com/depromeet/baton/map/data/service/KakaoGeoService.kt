package com.depromeet.baton.map.data


import com.depromeet.baton.map.data.model.KakaoGeoResponse
import com.depromeet.baton.map.data.service.KakaoApiClient
import com.depromeet.map.BuildConfig
import retrofit2.*
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

class  KakaoGeoService {
    private val CORD ="WGS84"
    val client = KakaoApiClient.instance?.create(KakaoGeoAPI::class.java)
    suspend fun getAddressFromLocation(longitude : Double, latitude : Double)
    =client?.getAddress("KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",longitude.toString(),latitude.toString(),CORD)
}

interface KakaoGeoAPI {
    @GET("/v2/local/geo/coord2address.json")
   suspend fun getAddress(
        @Header("Authorization") key : String,
        @Query("x") xPos: String,
        @Query("y") yPos: String,
        @Query("input_coord") cord: String,
    ) :Response<KakaoGeoResponse>
}
