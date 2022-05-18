package com.depromeet.baton.map.domain.entity

class SearchAddressItemEntity(_item : List<AddressEntity>) {
    val item = _item
    fun mapToUi()
        = ArrayList<AddressEntity>().apply{
            this.addAll(item)
        }
}

