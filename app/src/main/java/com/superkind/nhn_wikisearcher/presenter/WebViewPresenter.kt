package com.superkind.nhn_wikisearcher.presenter

import android.view.KeyEvent
import android.webkit.WebViewClient

interface WebViewPresenter {
    interface View {
        // Activity
        fun getIntentData(): String

        // Layout
        fun initWebView(webViewClient: WebViewClient)
        fun loadWebView(url: String)
        fun setSearchText(search: String)
        fun getSearchText(): String

        // Loading
        fun showLoading(show: Boolean)
        fun showMsgDialog(show: Boolean, msg: String)

        // Action
        fun goBack()
        fun hideKeyboard()
    }

    interface Presenter {
        // Life Cycle
        fun onCreate()
        fun onResume()

        // Action
        fun onClickBack()
        fun onClickSearch()
        fun onKeySearch(keyCode: Int, event: KeyEvent?): Boolean
    }
}