package com.superkind.nhn_wikisearcher.presenter

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import com.superkind.nhn_wikisearcher.activity.SearchActivity
import com.superkind.nhn_wikisearcher.model.SslWebViewClient
import com.superkind.nhn_wikisearcher.util.Util

class WebViewPresenterImpl(var mContext: Context, var mView: WebViewPresenter.View)
    : WebViewPresenter.Presenter, SslWebViewClient.WebViewCallBack {
    val mWebViewClient = SslWebViewClient(this)

    override fun onCreate() {
        val searchText = mView.getIntentData()
        mView.setSearchText(Util.makeDisplayText(searchText))

        mView.initWebView(mWebViewClient)
        mView.loadWebView(searchText)
    }

    override fun onResume() {
        mView.hideKeyboard()
    }

    override fun onClickBack() {
        mView.goBack()
    }

    override fun onClickSearch() {
        val intent = Intent(mContext.applicationContext, SearchActivity::class.java)
        intent.putExtra("search", mView.getSearchText())
        mContext.startActivity(intent)
    }

    override fun onKeySearch(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
            onClickSearch()
            return true
        }
        return false
    }

    // WebViewCallBack
    override fun onReceivedSslError() {
    }

    // WebViewCallBack
    override fun shouldOverrideUrlLoading() {
    }

    override fun onPageStarted() {
        mView.showLoading(true)
        mView.hideKeyboard()
    }

    override fun onPageFinished() {
        mView.showLoading(false)
    }

}