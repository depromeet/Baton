package com.depromeet.baton.domain.model

import androidx.annotation.Keep

@Keep
data class TicketImage (
    val id : Int,
    val url : String,
    val thumbnailUrl : String,
    val isMain :Boolean
)
