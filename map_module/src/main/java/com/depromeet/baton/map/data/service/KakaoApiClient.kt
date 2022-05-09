package com.depromeet.baton.map.data.service

import android.util.Log
import com.depromeet.map.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException


object KakaoApiClient {
    private const val BASE_URL = "https://dapi.kakao.com"
    private var retrofit: Retrofit? = null
    val instance: Retrofit?
        get() {
            val logger = HttpLoggingInterceptor { Log.d("KakaoApiClient", it) }
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor( logger )
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


