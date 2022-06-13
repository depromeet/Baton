package com.depromeet.baton.remote.user

import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.*

interface UserInfoService {

    @PATCH("users/{id}/address")
    suspend fun updateUserAddress(
        @Path("id") userIdx : Int,
        @Body body: UserAddressRequest
    ) : Response<UserAddressResponse>

    @PATCH("users/{id}/account")
    suspend fun updateUserAccount(
        @Path("id") userIdx : Int,
        @Body body: UserAccount
    ) : Response<UserAccount>

    @GET("users/{id}/account")
    suspend fun getUserAccount(
        @Path("id") userIdx : Int,
    ) : Response<UserAccount>

    @POST("users/{id}/account")
    suspend fun postUserAccount(
        @Path("id") userIdx : Int,
        @Body body: UserAccount
    ) : Response<UserAccount>

}

data class UserAddressRequest(
    @Json(name="latitude")val latitude : Float,
    @Json(name="longitude")val longitude : Float,
    @Json(name="address")val address : String,
    @Json(name="detailed_address")val detailAddress :String
)

data class UserAddressResponse(
    @Json(name="address")val address : String,
    @Json(name="detailed_address")val detailAddress :String
)


data class UserAccount(
    @Json(name="holder")val holder: String,
    @Json(name="bank") val bank : String,
    @Json(name="number")val number :String
)