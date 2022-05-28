package com.depromeet.baton.map.domain.repository


import com.depromeet.baton.map.domain.entity.ShopEntity
import com.depromeet.baton.map.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface SearchShopRepository {
    suspend fun searchShop(query : String) : Flow<NetworkResult<ArrayList<ShopEntity>>>
}