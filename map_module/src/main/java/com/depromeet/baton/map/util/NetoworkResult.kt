package com.depromeet.baton.map.util

sealed class NetworkResult<T>(
    val data: T? = null,
    val code : Int?=null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, code : Int? = null) : NetworkResult<T>( code=code, message= message)
    class Loading<T> : NetworkResult<T>()
}

interface NetworkResultMapper<P,U>{
    fun mapper ( input :NetworkResult<P>) : NetworkResult<U>
}


