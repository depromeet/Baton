package com.depromeet.baton.domain.model

import com.squareup.moshi.Json

//BEAN: 이거 중간 중간 저장해야함.
class SignUpKakaoRequest(
    val uid: String,
    val provider: String,
    val user: User,
) {
    data class User(
        val name: String,
        val nickname: String,
        @Json(name = "phone_number") val phoneNumber: String,
        val latitude: Float,
        val longitude: Float,
        val address: String,
        @Json(name = "detailed_address") val detailedAddress: String,
        @Json(name = "check_terms_of_service") val checkTermsOfService: Boolean,
        @Json(name = "check_privacy_policy") val checkPrivacyPolicy: Boolean,
        val account: Account?,
    )

    data class Account(
        val holder: String,
        val bank: String,
        val number: String
    )
}