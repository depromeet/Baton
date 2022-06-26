package com.depromeet.baton.domain.model

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
data class UserResponse(
    @Json(name = "id") val id: Int
)
