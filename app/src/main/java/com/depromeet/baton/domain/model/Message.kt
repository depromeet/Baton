package com.depromeet.baton.domain.model

import com.depromeet.baton.R

data class Message (
    val id : Int ?,
    val image : String?,
    val category : String,
    val gymName : String,
    val nickName : String,
    val content : String,
    val date : String,
    val isChecked : Boolean?,
    val empty : Int = com.depromeet.bds.R.drawable.img_profile_basic_smile_56
)