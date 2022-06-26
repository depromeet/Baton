package com.depromeet.baton.map.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ResponseNaverLocal(
    var lastBuildDate: String = "",
    var total: Int = 0,
    var start: Int = 0,
    var display: Int = 0,
    var category : String="",
    var items: List<Items>
){
    @Keep
    data class Items(
        var title : String = "",
        var link : String = "",
        var category : String="",
        var description : String = "",
        var telephone :String="",
        var roadAddress : String = "",
        var address : String = "",
        var mapx : Int =0,
        var mapy : Int =0
    )
}