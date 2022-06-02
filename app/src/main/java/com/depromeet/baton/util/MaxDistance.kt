package com.depromeet.baton.util

class MaxDistance( private val distance : Int ){
    //m로 변환된 값
    val unit = if(distance>=1000) "km" else "m"

    fun getDistanceToKM() : Float{
        return distance.toFloat() /1000.0f
    }

    fun getDistance() : Int = distance

    fun getMaxDistanceWithUnit() : String {
        return if(unit=="km")
            String.format("%1.1f",(distance.toFloat()/1000.0f))+unit
        else distance.toString()+unit
    } // unit을 고려해 출력 ex) 1.3km , 900m
}
