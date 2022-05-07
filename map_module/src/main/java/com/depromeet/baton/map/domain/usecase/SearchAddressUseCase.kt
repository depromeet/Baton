package com.depromeet.baton.map.domain.usecase

import com.depromeet.baton.map.domain.entity.AddressEntity
import com.depromeet.baton.map.domain.repository.AddressRepository
import com.depromeet.baton.map.domain.repository.SearchAddressRepository
import com.depromeet.baton.map.util.NetworkResult
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class SearchAddressUseCase @Inject constructor(private val repository : SearchAddressRepository) {
    suspend  fun searchAddress(query : String) = flow<SearchItem>{
        repository.searchAddress(query).collect{
            when(it){
                is NetworkResult.Success -> {
                    if(it.data!!.item.isEmpty()) emit(SearchItem.Empty)
                    else emit(SearchItem.Content(it.data!!.mapToUi()))
                }
                else -> {
                    emit(SearchItem.Empty)
                }
            }
        }
    }
}

sealed class SearchItem ( val data:  ArrayList<AddressEntity>? = null){
    class Content(data : ArrayList<AddressEntity>) : SearchItem(data)
    object Empty : SearchItem()
}

