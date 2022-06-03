package com.depromeet.baton.map.data.repositoryImpl

import com.depromeet.baton.map.data.dataSource.SearchDataSource
import com.depromeet.baton.map.data.model.SearchAddressModel
import com.depromeet.baton.map.domain.entity.SearchAddressItemEntity
import com.depromeet.baton.map.domain.repository.SearchAddressRepository
import com.depromeet.baton.map.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchAddressRepositoryImpl(private val searchDataSource: SearchDataSource) :SearchAddressRepository{

    override suspend fun searchAddress(query: String)=flow<NetworkResult<SearchAddressItemEntity>> {
        searchDataSource.searchAddress(query = query).collect{
            when(it){
                is NetworkResult.Success -> {
                    val res = SearchAddressModel(it.data!!)
                    emit(NetworkResult.Success(res.mapToDomain()))
                }
                is NetworkResult.Error ->{
                    emit(NetworkResult.Error(it.message.toString()))
                }
            }
        }
    }.flowOn(Dispatchers.IO)
        .catch { e -> emit(NetworkResult.Error(e.message.toString())) }

}