package com.superkind.nhn_wikisearcher.model

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient


class SslWebViewClient(var mCallBack: WebViewCallBack?) : WebViewClient() {
    interface WebViewCallBack {
        fun onReceivedSslError()
        fun shouldOverrideUrlLoading()
        fun onPageStarted()
        fun onPageFinished()
    }


    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        handler.proceed() // 계속 진행
        mCallBack?.onReceivedSslError()
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        mCallBack?.shouldOverrideUrlLoading()
        return true
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        mCallBack?.onPageStarted()
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        mCallBack?.onPageFinished()
    }
}