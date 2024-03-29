package com.depromeet.baton.domain.model

import com.depromeet.baton.R

data class Message (
    val id : Int ?,
    val type : MsgType,
    val image : String?,
    val category : String,
    val gymName : String,
    val nickName : String,
    val content : String,
    val date : String,
    val isChecked : Boolean?,
)

enum class MsgType(val type : String){ SEND("send") , RCV("receive") }