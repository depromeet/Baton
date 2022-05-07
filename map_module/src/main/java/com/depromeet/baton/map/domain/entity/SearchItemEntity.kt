package com.depromeet.baton.map.domain.entity

import com.depromeet.baton.map.data.model.SearchAddressModel

class SearchItemEntity(_item : List<AddressEntity>) {
    val item = _item
    fun mapToUi()
        = ArrayList<AddressEntity>().apply{
            this.addAll(item)
        }
}