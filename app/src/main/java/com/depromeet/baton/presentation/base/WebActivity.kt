package com.depromeet.baton.presentation.base

import android.os.Bundle
import android.webkit.WebView
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityWebviewBinding

class WebActivity : BaseActivity<ActivityWebviewBinding>(R.layout.activity_webview) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nWebView :WebView =  findViewById(R.id.webView);
        nWebView.loadUrl("http://www.naver.com")
    }
}