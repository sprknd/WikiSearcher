package com.superkind.nhn_wikisearcher.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.superkind.nhn_wikisearcher.constant.WikiURL
import com.superkind.nhn_wikisearcher.databinding.ActivityWebviewBinding
import com.superkind.nhn_wikisearcher.model.SslWebViewConnect
import com.superkind.nhn_wikisearcher.util.Util

class WebViewActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityWebviewBinding.inflate(layoutInflater)

        val detail = getIntentData()

        initWebView()
        loadWebView(detail)

        setContentView(mBinding.root)
    }

    private fun getIntentData() : String {
        val detail = intent.getStringExtra("search") ?: return ""
        mBinding.searchBar.setSearchText(detail) // Intent로 들어온 값을 EditText에 지정합니다


        return Util.makeSearchText(detail)
    }

    private fun initWebView() {
        mBinding.searchBar.setSearchTextEnabled(false)

        mBinding.webView.webViewClient = SslWebViewConnect()
        mBinding.webView.setInitialScale(1)

        val webViewSetting = mBinding.webView.settings
        with (webViewSetting) {
            setSupportMultipleWindows(false)
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            domStorageEnabled = true
        }
    }

    private fun loadWebView(url: String) {
        if (url.isNotEmpty()) {
            mBinding.webView.loadUrl(WikiURL.detail + url)
        }
    }
}