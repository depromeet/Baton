package com.depromeet.baton.map.data.dataSource

import com.depromeet.baton.map.base.BaseApiResponse
import com.depromeet.baton.map.data.model.AddressResult
import com.depromeet.baton.map.data.service.SearchAddressService
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.map.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject

class SearchDataSource @Inject constructor() : BaseApiResponse() {
    private val searchService = SearchAddressService().client!!

    suspend fun searchAddress(query : String) = flow<NetworkResult<AddressResult>>{
        Timber.e("Send request")
        emit(  safeApiCall { searchService.searchAddress(BuildConfig.ADDRESS_API_KEY,query) })
    }.flowOn(Dispatchers.IO)
        .catch { e -> emit(NetworkResult.Error(e.toString(),null)) }.flowOn(Dispatchers.IO)
}