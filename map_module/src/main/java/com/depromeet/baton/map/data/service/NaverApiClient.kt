package com.depromeet.baton.map.data.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NaverApiClient {
    private const val BASE_URL = "https://openapi.naver.com/v1/"
    private var retrofit: Retrofit? = null
    val instance: Retrofit?
        get() {
            val client = OkHttpClient.Builder()
                .build()
            val moshiBuilder = Moshi.Builder().add(KotlinJsonAdapterFactory()) .build()
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshiBuilder))
                .client(client)
                .build()
            return retrofit
        }
}
