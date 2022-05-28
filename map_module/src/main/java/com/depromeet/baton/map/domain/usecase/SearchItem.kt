package com.depromeet.baton.map.domain.usecase

import com.depromeet.baton.map.domain.entity.ShopEntity

sealed class SearchItem<T> ( val data: T? = null){
    class Content<T>(data : T) : SearchItem<T>(data)
    class Empty<T> : SearchItem<T>()
}
