package com.depromeet.baton.map.data.service

import com.depromeet.baton.map.data.model.response.ResponseNaverLocal
import com.depromeet.map.BuildConfig
import retrofit2.Response
import retrofit2.http.*

interface NaverMapAPI{

    @GET("search/{type}")
    suspend fun getSearchLocalResult(
        @Header("X-Naver-Client-Id") id:String? = ID,
        @Header("X-Naver-Client-Secret") pw:String? =PASSWORD,
        @Path("type") type: String? = "local.json",
        @Query("query") query: String?="",
        @Query("display") display: Int?=5
    ): Response<ResponseNaverLocal>

    companion object{
        val ID =BuildConfig.NAVER_API_CLIENT_ID_KEY
        val PASSWORD =BuildConfig.NAVER_API_CLIENT_SECRET_KEY
    }
}
