package com.depromeet.baton.presentation.ui.detail.model

import com.depromeet.baton.domain.model.AdditionalOptions
import com.depromeet.baton.domain.model.HashTag
import com.depromeet.baton.domain.model.TransactionMethod

data class MarketBasicInfo(
    val seller : Seller,
    val gymName : String,
    val price : String,
    val canSuggest : Boolean,
    val registration_date : String,
    val views: String,
    val likes : String,
    val url: String,
    val detailContent : String,
    val transactionMethod:TransactionMethod,
    val moreTag : ArrayList<AdditionalOptions>  //tag
)

data class Seller(
    val name : String?="",
    val nickname : String,
    val profile : String?="",
    val joinDate :String
)

