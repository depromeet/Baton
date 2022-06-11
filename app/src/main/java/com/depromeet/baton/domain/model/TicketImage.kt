package com.depromeet.baton.domain.model

data class TicketImage (
    val id : Int,
    val url : String,
    val thumbnailUrl : String,
    val isMain :Boolean
)
