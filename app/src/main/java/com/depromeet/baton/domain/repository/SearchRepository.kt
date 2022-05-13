package com.depromeet.baton.domain.repository

import com.depromeet.baton.domain.model.BatonHashTag
import com.depromeet.baton.domain.model.RecentSearchKeyword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor() {
    private val ioDispatcher = Dispatchers.IO

    private val keywords: MutableStateFlow<List<RecentSearchKeyword>> =
        MutableStateFlow(emptyList())

    fun getRecentSearchKeywords(): Flow<List<RecentSearchKeyword>> {
        return keywords
    }

    suspend fun getRecommendHashTags(): List<BatonHashTag> {
        return HAST_TAGS
    }

    suspend fun addRecentSearchKeyword(keyword: RecentSearchKeyword) {
        withContext(ioDispatcher) {
            keywords.update { prev ->
                listOf(keyword) + prev.filter { it.title != keyword.title }
            }
        }
    }

    suspend fun removeRecentSearchKeyword(keyword: RecentSearchKeyword) {
        withContext(ioDispatcher) {
            keywords.update { prev ->
                prev.filter { it.title != keyword.title }
            }
        }
    }

    suspend fun clearAllRecentSearchKeywords() {
        withContext(ioDispatcher) {
            keywords.update { emptyList() }
        }
    }

    companion object {
        private val HAST_TAGS = listOf(
            BatonHashTag("친절한 선생님"),
            BatonHashTag("체계적인 수업"),
            BatonHashTag("맞춤케어"),
            BatonHashTag("넓은 시설"),
            BatonHashTag("다양한 기구"),
            BatonHashTag("사람이 많은"),
            BatonHashTag("조용한 분위기"),
            BatonHashTag("역세권"),
            BatonHashTag("최신 기구"),
            BatonHashTag("쾌적한 환경"),
            BatonHashTag("사람이 적은"),
        )
    }
}

