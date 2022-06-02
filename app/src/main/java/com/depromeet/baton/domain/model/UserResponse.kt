package com.depromeet.baton.domain.model

import com.squareup.moshi.Json

data class UserResponse(
    @Json(name = "id") val id: Int
)
