package com.depromeet.baton.map.domain.repository

import com.depromeet.baton.map.domain.entity.SearchAddressItemEntity
import com.depromeet.baton.map.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface SearchAddressRepository {
    suspend fun searchAddress(query: String) : Flow<NetworkResult<SearchAddressItemEntity>>
}