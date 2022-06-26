package com.depromeet.baton.domain.api.notification

import com.depromeet.baton.remote.fcm.FcmData
import com.depromeet.baton.remote.fcm.FcmService
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmApi @Inject constructor(private val service :FcmService) {
    suspend fun postFcmData(token :String, title: String, body: String) : Response<String> {
        return service.postFcmData(FcmData(token, title, body))
    }
}