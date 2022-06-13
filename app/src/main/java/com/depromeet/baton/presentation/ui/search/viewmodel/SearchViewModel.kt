package com.depromeet.baton.presentation.ui.search.viewmodel

import androidx.lifecycle.MutableLiveData
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.ui.search.view.SearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : BaseViewModel() {

    val lastSearchKeyword = MutableLiveData<String>("")

    private val _searchKeyword: MutableStateFlow<String> = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private val _searchUiState: MutableStateFlow<SearchUiState> = MutableStateFlow(createState())
    val searchUiState = _searchUiState.asStateFlow()

    private val _currentLevel = MutableLiveData(1)

    fun searchKeyword(keyword: String) {
        _searchKeyword.tryEmit(keyword)
    }

    fun setLastKeyword(keyword: String) {
        lastSearchKeyword.value = keyword
    }

    fun setCurrentLevel(position: Int) {
        _currentLevel.value = position
    }

    private fun createState(): SearchUiState {
        return SearchUiState(
            onBackBtnClick = ::handleBackBtnClick,
        )
    }

    private fun handleBackBtnClick() {
        when (_currentLevel.value) {
            0 -> addViewEvent(ViewEvent.ToHome)
            1 -> addViewEvent(ViewEvent.ToRecentSearch)
        }
    }

    private fun addViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it + viewEvent }
    }

    fun consumeViewEvent(viewEvent: ViewEvent) {
        _viewEvents.update { it - viewEvent }
    }

    sealed interface ViewEvent {
        object ToRecentSearch : ViewEvent
        object ToHome : ViewEvent
    }
}

