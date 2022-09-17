package com.depromeet.baton.map.data.model

import androidx.annotation.Keep
import com.depromeet.baton.map.data.model.response.ResponseNaverLocal
import com.depromeet.baton.map.domain.entity.*
import com.naver.maps.geometry.Tm128
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = false)
class SearchShopModel(_response : ResponseNaverLocal) {
    val response =_response

    fun mapToDomain() : ArrayList<ShopEntity> {
        val result =ArrayList<ShopEntity>()
        if(response.total ==0) return (result)  //empty list 전달
        response.items.forEach {
            run{
                if(it.category.contains("스포츠") || it.category.contains("운동")){
                    val name = it.title
                        .replace("<b>", "")
                        .replace("</b>", " ")
                        .replace("&amp;","")
                    val gps= Tm128(it.mapx.toDouble(), it.mapy.toDouble()).toLatLng()
                    val location = LocationEntity(gps.latitude, gps.longitude,it.address, it.roadAddress )
                    result.add(ShopEntity(name,it.link,it.category,location))
                }
             }
        }
        return result
    }
}