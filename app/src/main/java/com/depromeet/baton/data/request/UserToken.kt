package com.depromeet.baton.data.request

import androidx.annotation.Keep

@Keep
data class UserToken(
    val fcmToken: String
)