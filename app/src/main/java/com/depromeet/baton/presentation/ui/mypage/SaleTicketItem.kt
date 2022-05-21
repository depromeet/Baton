package com.depromeet.baton.presentation.ui.mypage

data class SaleTicketItem (
    val shopName: String,
    val card: String,
    val price: String,
    val remainingDay: String,
    val place: String,
    val distance: String,
    val img: Int,
    val status: String
)