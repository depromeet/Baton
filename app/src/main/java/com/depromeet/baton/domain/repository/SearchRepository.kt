package com.depromeet.baton.domain.repository

import com.depromeet.baton.domain.model.HashTag
import com.depromeet.baton.domain.model.RecentSearchKeyword
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository {
    private val ioDispatcher = Dispatchers.IO

    suspend fun getRecentSearchKeywords(): List<RecentSearchKeyword> {
        withContext(ioDispatcher) {
            TODO()
        }
    }

    suspend fun getRecommendHashTags(): List<HashTag> {
        withContext(ioDispatcher) {
            TODO()
        }
    }

    suspend fun addRecentSearchKeyword(keyword: RecentSearchKeyword) {
        withContext(ioDispatcher) {
            TODO()
        }
    }

    suspend fun clearAllRecentSearchKeywords() {
        withContext(ioDispatcher) {
            TODO()
        }
    }
}

