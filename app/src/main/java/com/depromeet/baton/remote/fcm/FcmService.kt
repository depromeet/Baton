package com.depromeet.baton.remote.fcm

import androidx.annotation.Keep
import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmService {
    @POST("/fcm")
    suspend fun postFcmData(
        @Body body : FcmData
    ) : Response<String>
}

@Keep
data class FcmData(
    @Json(name = "targetToken") val token : String,
    @Json(name="title")val title : String,
    @Json(name="body")val body : String
)