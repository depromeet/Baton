package com.depromeet.baton.map.base

import com.depromeet.baton.map.util.NetworkResult
import retrofit2.Response

abstract class BaseApiResponse {
    suspend fun <T> safeApiCall(apiCall:suspend() -> Response<T>): NetworkResult<T> {
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(body)
                }
            }
            return error("${response.code()} ${response.message()}",code =response.code())
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }
    private fun <T> error(errorMessage: String,code:Int?=null): NetworkResult<T> =
        NetworkResult.Error("Api call failed $errorMessage",code=code)
}