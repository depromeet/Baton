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
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }
}


