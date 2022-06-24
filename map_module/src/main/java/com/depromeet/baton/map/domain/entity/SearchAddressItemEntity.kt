package com.depromeet.baton.map.domain.entity

import androidx.annotation.Keep

@Keep
class SearchAddressItemEntity(_item : List<AddressEntity>) {
    val item = _item
    fun mapToUi()
        = ArrayList<AddressEntity>().apply{
            this.addAll(item)
        }
}

