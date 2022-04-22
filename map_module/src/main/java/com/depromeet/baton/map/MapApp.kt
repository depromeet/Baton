package com.depromeet.baton.map

import android.app.Application
import com.naver.maps.map.NaverMapSdk
import timber.log.Timber

class MapApp: Application() {
    override fun onCreate() {
        super.onCreate()
        NaverMapSdk.getInstance(this).client =
            NaverMapSdk.NaverCloudPlatformClient(BuildConfig.NAVER_SDK_CLIENT_KEY)
        initLogger()
    }

    private fun initLogger() {
      if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}