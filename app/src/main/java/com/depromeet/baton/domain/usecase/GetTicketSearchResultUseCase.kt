package com.depromeet.baton.domain.usecase

import com.depromeet.baton.data.mapper.SearchMapper
import com.depromeet.baton.domain.repository.SearchRepository
import com.depromeet.baton.presentation.base.UIState
import com.depromeet.baton.util.BatonSpfManager
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetTicketSearchResultUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val spfManager: BatonSpfManager
) {

    suspend fun execute(
        page: Int,
        size: Int,
        latitude: Float = spfManager.getLocation().latitude.toFloat(),
        longitude: Float = spfManager.getLocation().longitude.toFloat(),
        query: String,
        maxDistance: Int = spfManager.getMaxDistance().getDistance(),
    ): UIState {
        return SearchMapper.mapperToFilteredTicket(
            searchRepository.getTicketSearchResult(
                page = page,
                size = size,
                latitude = latitude,
                longitude = longitude,
                query = query,
                maxDistance = maxDistance,
            )
        )
    }
}
