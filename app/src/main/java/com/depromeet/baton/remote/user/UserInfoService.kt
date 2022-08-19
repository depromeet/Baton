package com.depromeet.baton.remote.user

import android.os.Parcelable
import androidx.annotation.Keep
import com.depromeet.baton.data.request.UserToken
import com.depromeet.baton.data.response.*
import com.depromeet.baton.domain.model.MypageTicketResponse
import com.depromeet.baton.domain.model.TicketSimpleInfo
import com.depromeet.baton.remote.ticket.MypageBasicResponse
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import retrofit2.Response
import retrofit2.http.*

interface UserInfoService {
    @GET("user/users/{id}")
    suspend fun getUserProfile(@Path("id") userIdx : Int) : Response<UserProfileResponse>

    @PUT("user/users/{id}")
    suspend fun updateUserProfile(
        @Path("id") userIdx : Int,
        @Body body : UserProfileRequest
    ) : Response<UserProfileRequest>

    @DELETE("socialusers/{id}")
    suspend fun deleteUser(@Path("id") userIdx : Int)
    :Response<MypageBasicResponse>

    @GET("user/users/{id}/bookmarks")
    suspend fun getUserBookmarks(
        @Path("id") bookmarkId: Int,
        @Query("state")state :Int?=0

    ) : Response<List<BookmarkTicket>>

    @GET("user/users/{id}/buys")
    suspend fun getUserBuyTickets(@Path("id") userIdx : Int): Response<List<UserBuyListResponse>>

    @GET("user/users/{id}/sells")
    suspend fun getUserSellTickets(
        @Path("id") userIdx : Int,
        @Query("state") state :Int?=0
    ) : Response<List<MypageTicketResponse>>

    @PATCH("user/users/{id}/address")
    suspend fun updateUserAddress(
        @Path("id") userIdx : Int,
        @Body body: UserAddressRequest
    ) : Response<UserAddressResponse>

    @PATCH("user/users/{id}/account")
    suspend fun updateUserAccount(
        @Path("id") userIdx : Int,
        @Body body: UserAccount
    ) : Response<UserAccount>

    @GET("user/users/{id}/account")
    suspend fun getUserAccount(
        @Path("id") userIdx : Int,
    ) : Response<UserAccount>

    @POST("user/users/{id}/account")
    suspend fun postUserAccount(
        @Path("id") userIdx : Int,
        @Body body: UserAccount,
    ) : Response<UserAccount>

    @PATCH("user/users/{id}")
    suspend fun updateDeviceToken(
        @Path("id") userIdx : Int,
        @Body body: UserToken,
    ) : Response<ResponseUserToken>
}

@Keep
data class UserProfileRequest(
    @Json(name="nickname") val nickname :String,
    @Json(name="phone_number")val phoneNum : String
)

@Keep
data class UserAddressRequest(
    @Json(name="latitude")val latitude : Float,
    @Json(name="longitude")val longitude : Float,
    @Json(name="address")val address : String,
    @Json(name="detailed_address")val detailAddress :String
)

@Keep
data class UserAddressResponse(
    @Json(name="address")val address : String,
    @Json(name="detailed_address")val detailAddress :String
)

@Keep
@Parcelize
data class UserAccount(
    @Json(name="holder")val holder: String,
    @Json(name="bank") val bank : String,
    @Json(name="number")val number :String
):Parcelable
