package com.depromeet.baton

import android.app.Application
import com.depromeet.baton.util.BatonSpfManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.common.KakaoSdk
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BatonApp : Application() {

    @Inject
    lateinit var spfManager: BatonSpfManager

    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_SDK_CLIENT_KEY)
        initLogger()
        initKakao()
        getDeviceToken()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun initKakao() {
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key), loggingEnabled = true)
    }

    private fun getDeviceToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            Timber.d("baton-fcm: ${token}")
            spfManager.setDeviceToken(token.toString())
        })
    }

    companion object {
        const val TAG: String = "BATON-APP"
    }
}
