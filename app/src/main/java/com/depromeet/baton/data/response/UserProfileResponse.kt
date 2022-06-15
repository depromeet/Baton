package com.depromeet.baton.data.response

import com.depromeet.baton.remote.user.UserAccount
import com.squareup.moshi.Json

data class UserProfileResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "phone_number") val phone_number: String,
    @Json(name = "created_on") val created_on : String,
    @Json(name = "account") val account: UserAccount?
)
