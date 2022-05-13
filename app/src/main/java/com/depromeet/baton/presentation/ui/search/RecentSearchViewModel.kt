package com.depromeet.baton.presentation.ui.search

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.domain.model.BatonHashTag
import com.depromeet.baton.domain.model.RecentSearchKeyword
import com.depromeet.baton.domain.repository.SearchRepository
import com.depromeet.baton.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecentSearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : BaseViewModel() {

    val recentSearchKeywords = searchRepository.getRecentSearchKeywords()

    val recommendedHashTags = flow<List<BatonHashTag>> {
        emit(searchRepository.getRecommendHashTags())
    }

    fun searchKeyword(keyword: RecentSearchKeyword) {
        viewModelScope.launch {
            searchRepository.addRecentSearchKeyword(keyword)
        }
    }

    fun deleteKeyword(keyword: RecentSearchKeyword) {
        viewModelScope.launch {
            searchRepository.removeRecentSearchKeyword(keyword)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            searchRepository.clearAllRecentSearchKeywords()
        }
    }
}
