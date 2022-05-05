package com.depromeet.baton.map.util

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
    class Loading<T> : NetworkResult<T>()
}

interface NetworkResultMapper<P,U>{
    fun mapper ( input :NetworkResult<P>) : NetworkResult<U>
}


