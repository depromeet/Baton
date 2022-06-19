package com.depromeet.baton.presentation.ui.mypage.viewmodel

import androidx.lifecycle.viewModelScope
import com.depromeet.baton.data.response.BookmarkTicket
import com.depromeet.baton.domain.di.IoDispatcher
import com.depromeet.baton.domain.di.MainDispatcher
import com.depromeet.baton.domain.repository.AuthRepository
import com.depromeet.baton.domain.repository.BookmarkRepository
import com.depromeet.baton.domain.repository.UserinfoRepository
import com.depromeet.baton.map.util.NetworkResult
import com.depromeet.baton.presentation.base.BaseViewModel
import com.depromeet.baton.util.BatonSpfManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MyBookmarkViewModel @Inject constructor(
    private val spfManager: BatonSpfManager,
    @IoDispatcher private val Dispatcher: CoroutineDispatcher,
    private val bookmarkRepository: BookmarkRepository,
    private val authRepository: AuthRepository,
    private val userinfoRepository: UserinfoRepository) : BaseViewModel() {

    private val _uiState = MutableStateFlow<BookmarkUiState>(BookmarkUiState(emptyList(),true))
    val uiState = _uiState.asStateFlow()

    private val _netWorkState = MutableStateFlow<NetworkResult<List<BookmarkTicket>>>(NetworkResult.Loading())
    val netWorkState =_netWorkState.asStateFlow()


    fun getBookMarkList(){
       viewModelScope.launch{
            runCatching {
                userinfoRepository.getUserBookmarks(authRepository.authInfo!!.userId,0)
            }.onSuccess {
                    result -> _netWorkState.update { result }
                        when(result) {
                            is NetworkResult.Success -> {
                                _uiState.update { it.copy(list = result.data!!, isLoading = false) }
                            }
                            is NetworkResult.Error -> {
                                _uiState.update { it.copy(isLoading = false) }
                                Timber.e(result.message)
                            }
                        }
            }.onFailure {
                Timber.e(it.message)
                _netWorkState.update { NetworkResult.Error(it.message.toString()) }
            }
        }
    }


    fun deleteBookMark(bookmarkId : Int){
        viewModelScope.launch {
            runCatching {
                bookmarkRepository.deleteBookmark(bookmarkId)
            }.onSuccess {
                res ->
                    when(res){
                        is NetworkResult.Success->  {  }
                        is NetworkResult.Error->  Timber.e("${res.message}")
                    }
                getBookMarkList()
            }.onFailure {
                Timber.e(it.message)
            }
        }
    }
}

data class BookmarkUiState(
    var list : List<BookmarkTicket>,
    val isLoading : Boolean
){
    val isEmpty = list.isEmpty() && !isLoading
}


