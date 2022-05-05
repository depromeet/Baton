package com.depromeet.baton.map.data.model

import com.depromeet.baton.map.domain.entity.AddressEntity
import com.depromeet.baton.map.domain.entity.SearchItemEntity

class SearchAddressModel (_response: AddressResult) {
    val response =_response
    val total_count :Int= response.common?.totalCount!!.toInt()

    //item list
    fun mapToDomain() : SearchItemEntity {
        if(total_count ==0) return SearchItemEntity(ArrayList<AddressEntity>())  //empty list 전달
        val item  = response.juso!!.map{
                it -> AddressEntity(it.jibunAddr!!, it.roadAddr!!)
        }
        return SearchItemEntity(item)
    }
}