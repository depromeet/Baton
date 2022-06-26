package com.depromeet.baton.map.data.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class KakaoGeoResponse (
    @SerializedName("meta")
    val meta : Meta,
    @SerializedName("documents")
    val documents : List<Documents>
){
    @Keep
    data class Meta (
        @SerializedName("total_count")
        val total_count : Int)

    @Keep
    data class Documents (
        @SerializedName("road_address")
        val road_address : RoadAddress?,
        @SerializedName("address")
        val address : KakaoAddress
    ){
        @Keep
        data class KakaoAddress(
            val address_name : String,
            val region_1depth_name: String,
            val region_2depth_name: String,
            val region_3depth_name : String,
            val mountain_yn : String,
            val main_address_no : String,
            val sub_address_no : String,
        )
        @Keep
        data class RoadAddress(
            val address_name : String,
            val region_1depth_name: String,
            val region_2depth_name: String,
            val region_3depth_name : String,
            val road_name : String,
            val underground_yn : String,
            val main_building_no : String,
            val sub_building_no : String,
            val building_name : String,
            val zone_no : String,
        )

    }


}

