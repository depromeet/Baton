package com.depromeet.baton.util

import com.depromeet.baton.BatonApp.Companion.mSharedPreferences
import com.depromeet.baton.map.domain.entity.AddressEntity

fun saveAddress(road : String, jibun : String){
    val editor = mSharedPreferences.edit()
    editor.putString("roadAddress", road)
    editor.putString("address",  jibun)
    editor.apply()
}

fun saveAddress(road : String, jibun : String, detail : String){
    val editor = mSharedPreferences.edit()
    editor.putString("roadAddress", road)
    editor.putString("address",  jibun)
    editor.putString("detailAddress",  detail)
    editor.apply()
}

fun getAddress() : AddressEntity{
    val road = mSharedPreferences.getString("roadAddress","")
    val jibun = mSharedPreferences.getString("address","")
    return AddressEntity(jibun!!, road!!)
}


fun saveDetailAddress(detailAddress : String){
    val editor = mSharedPreferences.edit()
    editor.putString("detailAddress",detailAddress)
    editor.apply()
}


fun saveSearchDistance(distance : String){
    val editor = mSharedPreferences.edit()
    editor.putString("searchDistance",distance)
    editor.apply()
}


fun getSearchDistance() :String? = mSharedPreferences.getString("searchDistance", "500m")