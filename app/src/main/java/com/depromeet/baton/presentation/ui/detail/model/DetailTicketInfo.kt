package com.depromeet.baton.presentation.ui.detail.model

import android.net.Uri
import com.depromeet.baton.domain.model.*
import com.depromeet.baton.presentation.util.distanceFormatUtil

data class DetailTicketInfo (
    val ticketId : Int,
    val seller : Seller,
    val isOwner : Boolean,
    val location:DetailLocationInfo,
    val detailUrl : String,
    val mapUrl : String,
    val emptyIcon : Uri,
    val price : Int,
    val createdDate:String,
    val remainDate : Int?,
    val ticketStatus : TicketStatus,
    val ticketType : TicketKind,
    val transferFee: TransferFee,
    val transMethod :TradeType,
    val canNego : Boolean,
    val infoHashs : List<BatonHashTag>, //회원권 정보 태그
    val description : String,
    val tags : List<BatonHashTag>, //회원권 추가 정보 태그
    val imgList : List<TicketInfo.Image>,
    val isMembership : Boolean,
    val isHolding : Boolean,
    val remainingNumber : Int?,
    val isLikeTicket :Boolean,
    val bookmarkId : Int?,
    val bookmarkView : Int? =0,
    val countView :Int? =0
){
    data class Seller(
        val userId : Int,
        val nickname : String,
        val create_on :String,
        val image :String?
    )
}

data class DetailLocationInfo(
    val location : String,
    val address : String,
    val longitude : Double,
    val latitude : Double,
    val distance : Float
){
    val distanceGuide = "현재위치에서 ${distanceFormatUtil(distance.toDouble())} 떨어짐"
}

class DetailHash(
     val hashShower : Boolean,
     val hasLocker : Boolean,
     val hasClothes : Boolean,
     val hasGx : Boolean,
     val canResell : Boolean,
     val canRefund : Boolean,
     val isHolding: Boolean
){
    fun mapToHashList():List<BatonHashTag>{
        val hashs = ArrayList<BatonHashTag>()
        if(hashShower) hashs.add(BatonHashTag(HashInfo.HasShower.value))
        if(hasLocker)  hashs.add(BatonHashTag(HashInfo.HasLocker.value))
        if(hasClothes)  hashs.add(BatonHashTag(HashInfo.HasClothes.value))
        if(hasGx) hashs.add(BatonHashTag(HashInfo.HasGx.value))
        if(canResell)  hashs.add(BatonHashTag(HashInfo.CanResell.value))
        if(canRefund)hashs.add(BatonHashTag(HashInfo.CanRefund.value))
        if(isHolding)hashs.add(BatonHashTag(HashInfo.Holding.value))
        return hashs
    }
}

enum class HashInfo(val value: String ){
    HasShower("샤워실 포함"),HasLocker("락커룸 포함") ,HasClothes("운동복 포함"), HasGx("Gx포함")
    , CanResell("재양도 가능") , CanRefund("환불 가능"), Holding("홀딩(휴회)완료")
}