package com.depromeet.baton.util

class MaxDistance( _distance : Int ){
    val distance = _distance  //m로 변환된 값
    val unit = if(distance>=1000) "km" else "m"

    fun getKM() :Float{
        return if(distance>=1000)distance.toFloat() /1000.0f
        else distance.toFloat()
    }

    fun getM() : Int = distance

    fun getMaxDistanceWithUnit() : String {
        return if(unit=="km")
            String.format("%1.1f",(distance.toFloat()/1000.0f))+unit
        else distance.toString()+unit
    } // unit을 고려해 출력 ex) 1.3km , 900m

}
