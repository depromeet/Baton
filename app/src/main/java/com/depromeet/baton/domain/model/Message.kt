package com.depromeet.baton.domain.model

data class Message (
    val image : String,
    val category : String,
    val gymName : String,
    val nickName : String,
    val content : String,
    val date : String,
    val isChecked : Boolean
)