package com.depromeet.baton.data.request

import androidx.annotation.Keep

@Keep
data class UserToken(
    val fcm_token: String
)