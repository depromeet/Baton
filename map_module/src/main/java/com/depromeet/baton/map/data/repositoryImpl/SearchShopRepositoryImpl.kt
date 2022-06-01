package com.depromeet.baton.map.data.repositoryImpl


import com.depromeet.baton.map.data.dataSource.SearchDataSource
import com.depromeet.baton.map.data.model.SearchShopModel
import com.depromeet.baton.map.domain.entity.ShopEntity
import com.depromeet.baton.map.domain.repository.SearchShopRepository
import com.depromeet.baton.map.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SearchShopRepositoryImpl (private val searchDataSource: SearchDataSource) :SearchShopRepository{
    override suspend fun searchShop(query: String) = flow<NetworkResult<ArrayList<ShopEntity>>> {
        searchDataSource.searchShop(query = query).collect{

            when(it){
                is NetworkResult.Success -> {
                    val res = SearchShopModel(it.data!!)
                    emit(NetworkResult.Success(res.mapToDomain()))
                }
                is NetworkResult.Error ->{
                    emit(NetworkResult.Error(it.message.toString()))
                }
            }
        }
    }.flowOn(Dispatchers.IO)
        .catch { e -> emit(NetworkResult.Error(e.toString())) }

}