package com.depromeet.baton.data.response

import com.squareup.moshi.Json

data class UserProfileResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "phone_number") val phone_number: String,
    @Json(name = "created_on") val created_on : String,
    @Json(name = "account") val account: Account?
){

    data class Account(
        @Json(name = "holder") val holder: String ,
        @Json(name = "bank") val bank: String ,
        @Json(name = "number") val number: String ,
    )
}

