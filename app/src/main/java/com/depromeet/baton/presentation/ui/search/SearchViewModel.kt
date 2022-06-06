package com.depromeet.baton.presentation.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.presentation.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : BaseViewModel() {

    val _initSearchKeyword = MutableLiveData<String>("")
    val _lastSearchKeyword = MutableLiveData<String>("")

    private val _afterKeyword = MutableLiveData<String>()
    val afterKeyword: LiveData<String> get() = _afterKeyword

    private val _searchKeyword: MutableStateFlow<String> = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword

    private val _viewEvents: MutableStateFlow<List<ViewEvent>> = MutableStateFlow(emptyList())
    val viewEvents = _viewEvents.asStateFlow()

    private val _searchUiState: MutableStateFlow<SearchUiState> = MutableStateFlow(createState())
    val searchUiState = _searchUiState.asStateFlow()

    private val _currentLevel = MutableLiveData(1)

    //선택 완료 이벤트->다이어로그 닫힘
    private val _isNewKeyword = SingleLiveEvent<Any>()
    val isNewKeyword: LiveData<Any> = _isNewKeyword

    fun searchKeyword(keyword: String) {
        _searchKeyword.tryEmit(keyword)
    }

    fun isNewKeyword() {
        if (_lastSearchKeyword.value != _initSearchKeyword.value)
            _isNewKeyword.call()
    }

    fun setLastKeyword(keyword: String) {
        _lastSearchKeyword.value = keyword
        isNewKeyword()
    }

    fun setInitKeyword(keyword: String) {
        _initSearchKeyword.value = keyword
        isNewKeyword()
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

