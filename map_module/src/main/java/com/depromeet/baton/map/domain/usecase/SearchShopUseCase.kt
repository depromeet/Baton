package com.depromeet.baton.map.domain.usecase

import com.depromeet.baton.map.domain.entity.AddressEntity
import com.depromeet.baton.map.domain.entity.ShopEntity
import com.depromeet.baton.map.domain.repository.SearchShopRepository
import com.depromeet.baton.map.util.NetworkResult
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchShopUseCase @Inject constructor(private val repository: SearchShopRepository) {
    suspend fun searchShop(query :String)= flow<SearchItem<ArrayList<ShopEntity>>>{
        repository.searchShop(query).collect{
            when(it){
                is NetworkResult.Success -> {
                    if(it.data!!.isEmpty()) emit(SearchItem.Empty())
                    else emit(SearchItem.Content(it.data!!))
                }
                else -> {
                    emit(SearchItem.Empty())
                }
            }
        }
    }
}