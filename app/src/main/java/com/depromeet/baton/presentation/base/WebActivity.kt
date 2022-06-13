package com.depromeet.baton.presentation.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebView
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature
import com.depromeet.baton.R
import com.depromeet.baton.databinding.ActivityWebviewBinding
import com.depromeet.baton.presentation.ui.detail.TicketDetailActivity
import com.depromeet.bds.component.BdsToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class WebActivity : BaseActivity<ActivityWebviewBinding>(R.layout.activity_webview) {

    private lateinit var superSafeWebView: WebView
    private var safeBrowsingIsInitialized: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        superSafeWebView = WebView(this)
        superSafeWebView.webViewClient = MyWebViewClient()
        safeBrowsingIsInitialized = false

        if (WebViewFeature.isFeatureSupported(WebViewFeature.START_SAFE_BROWSING)) {
            WebViewCompat.startSafeBrowsing(this, ValueCallback<Boolean> { success ->
                safeBrowsingIsInitialized = true
                if (success) {
                    val url=  intent.getStringExtra("url")
                    if(url!=null) binding.webView.loadUrl(url)
                    else this.BdsToast("링크 정보가 올바르지 않습니다").show()

                    finish()
                }else{
                   this.BdsToast( "위험성 있는 페이지는 접근이 불가능합니다.").show()
                }
            })
        }
    }

    companion object{
        fun start(context: Context, url: String): Intent {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("url",url)
            return intent
        }
    }
}