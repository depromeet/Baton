package com.depromeet.baton.util

import com.depromeet.baton.BatonApp.Companion.mSharedPreferences
import com.depromeet.baton.map.domain.entity.AddressEntity
import com.naver.maps.geometry.LatLng

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

fun getDetailAddress() :String{
    val detail = mSharedPreferences.getString("detailAddress","")
    return detail!!
}

fun saveLocation(latLng: LatLng){
    val editor = mSharedPreferences.edit()
    editor.putFloat("latitude",latLng.latitude.toFloat())
    editor.putFloat("longitude",latLng.longitude.toFloat())
    editor.apply()
}

fun getLocation():LatLng{
    val latLng :LatLng = LatLng(mSharedPreferences.getFloat("latitude",0F).toDouble(),mSharedPreferences.getFloat("latitude",0F).toDouble())
    return latLng
}


fun saveMaxDistance(distance : Int){
    val editor = mSharedPreferences.edit()
    editor.putInt("maxDistance",distance)
    editor.apply()
}


fun getMaxDistance() : MaxDistance{
    val distance = mSharedPreferences.getInt("maxDistance", 500)
    return MaxDistance(distance)
}

