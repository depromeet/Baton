package com.depromeet.baton.presentation.base

import android.webkit.WebResourceRequest
import android.webkit.WebView
import androidx.webkit.SafeBrowsingResponseCompat
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewFeature
import com.depromeet.bds.component.BdsToast
import timber.log.Timber

class MyWebViewClient : WebViewClientCompat() {

    override fun onSafeBrowsingHit(
        view: WebView,
        request: WebResourceRequest,
        threatType: Int,
        callback: SafeBrowsingResponseCompat
    ) {

        if (WebViewFeature.isFeatureSupported(WebViewFeature.SAFE_BROWSING_RESPONSE_BACK_TO_SAFETY)) {
            callback.backToSafety(true)
            Timber.e( "위험성 있는 페이지는 접근이 불가능합니다.")
        }
    }
}
