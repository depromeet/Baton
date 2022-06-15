package com.depromeet.baton.remote.user

import android.os.Parcelable
import com.depromeet.baton.data.response.*
import com.depromeet.baton.domain.model.MypageTicketResponse
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import retrofit2.Response
import retrofit2.http.*

interface UserInfoService {
    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") userIdx : Int) : Response<UserProfileResponse>

    @PUT("users/{id}")
    suspend fun updateUserProfile(
        @Path("id") userIdx : Int,
        @Body body : UserProfileRequest
    ) : Response<UserProfileRequest>


    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userIdx : Int)

    @GET("users/{id}/bookmarks")
    suspend fun getUserBookmarks(
        @Path("id") bookmarkId: Int,
        @Query("state")state :Int?=0

    ) : Response<List<BookmarkTicket>>

    @GET("users/{id}/buys")
    suspend fun getUserBuyTickets(@Path("id") userIdx : Int): Response<List<UserBuyListResponse>>

    @GET("users/{id}/sells")
    suspend fun getUserSellTickets(
        @Path("id") userIdx : Int,
        @Query("state") state :Int?=0
    ) : Response<List<MypageTicketResponse>>

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
        @Body body: UserAccount,
    ) : Response<UserAccount>

}

data class UserProfileRequest(
    @Json(name="nickname") val nickname :String,
    @Json(name="phone_number")val phoneNum : String
)

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

@Parcelize
data class UserAccount(
    @Json(name="holder")val holder: String,
    @Json(name="bank") val bank : String,
    @Json(name="number")val number :String
):Parcelable
