package com.depromeet.baton.data.response
import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class ResponseAuthToken(
    val detail : String?=null,
    val code : String?=null,
    val token : String?=null
)

@Keep
data class ResponseRefreshToken(
    val access_token : String?=null,
    val refresh_token : String?=null,
    val detail : String?=null,
    val code : String?=null,
)