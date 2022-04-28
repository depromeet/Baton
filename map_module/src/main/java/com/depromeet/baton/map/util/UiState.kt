package com.depromeet.baton.map.util


sealed class UiState<T> {
    data class Success<T>(val data: T): UiState<T>()
    data class Error<T>(val error: Throwable?): UiState<T>()
    class Loading<T> : UiState<T>()
}

