package com.depromeet.baton.map.data.service

import android.util.Log
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit



object SearchAddressClient {
    private const val BASE_URL = "https://www.juso.go.kr"
    private var retrofit: Retrofit? = null
    val instance: Retrofit?
        get() {
            val logger = HttpLoggingInterceptor { Log.d("API", it) }
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val parser = TikXml.Builder()
                .exceptionOnUnreadXml(false).build()

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
          retrofit =  Retrofit.Builder()
                .baseUrl(BASE_URL.toHttpUrlOrNull()!!)
                .client(client)
                .addConverterFactory(TikXmlConverterFactory.create(parser))
                .build()
            return retrofit
        }

}


 class CustomInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("TEST", "${chain.request().method} - ${chain.request().url}")

        val request = chain.request().newBuilder().addHeader("Accept-Encoding", "identity").build()
        val response = chain.proceed(request)

        return response
    }
}