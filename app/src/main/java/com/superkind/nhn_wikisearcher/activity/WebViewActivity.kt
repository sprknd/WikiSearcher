package com.superkind.nhn_wikisearcher.activity

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.superkind.nhn_wikisearcher.constant.WikiURL
import com.superkind.nhn_wikisearcher.databinding.ActivityWebviewBinding
import com.superkind.nhn_wikisearcher.presenter.WebViewPresenter
import com.superkind.nhn_wikisearcher.presenter.WebViewPresenterImpl

class WebViewActivity : AppCompatActivity(), WebViewPresenter.View {
    private lateinit var mBinding: ActivityWebviewBinding
    private var mLoadingDialog: AlertDialog? = null
    private var mMsgDialog: AlertDialog? = null
    private var mPresenter = WebViewPresenterImpl(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityWebviewBinding.inflate(layoutInflater)

        initView()
        mPresenter.onCreate()

        setContentView(mBinding.root)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    override fun getIntentData() : String {
        return intent.getStringExtra("search") ?: ""
    }

    private fun initView() {
        mBinding.searchBar.setSearchOnClickListener {
            mPresenter.onClickSearch()
        }

        mBinding.searchBar.setBackOnClickListener {
            mPresenter.onClickBack()
        }

        mBinding.searchBar.setEditOnKeyListener { _, keyCode, event -> mPresenter.onKeySearch(keyCode, event) }
    }

    override fun initWebView(webViewClient: WebViewClient) {
        mBinding.webView.webViewClient = webViewClient
        mBinding.webView.setInitialScale(1)

        val webViewSetting = mBinding.webView.settings
        with (webViewSetting) {
            setSupportMultipleWindows(false)
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            domStorageEnabled = true
            displayZoomControls = true
        }
    }

    override fun loadWebView(url: String) {
        if (url.isNotEmpty()) {
            mBinding.webView.loadUrl(WikiURL.detail + url)
        }
    }

    override fun setSearchText(search: String) {
        mBinding.searchBar.setSearchText(search)
    }

    override fun getSearchText(): String {
        return mBinding.searchBar.getSearchText()
    }

    override fun showLoading(show: Boolean) {
        dismissDialogs()

        if (mLoadingDialog == null) {
            mLoadingDialog = AlertDialog.Builder(this)
                    .setMessage("Please wait a second..")
                    .setCancelable(false)
                    .create()
        }

        if (show) mLoadingDialog!!.show()
        else mLoadingDialog!!.dismiss()
    }

    override fun showMsgDialog(show: Boolean, msg: String) {
        dismissDialogs()

        if (mMsgDialog == null) {
            mMsgDialog = AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(msg)
                    .setPositiveButton("OK") { _, _ -> mMsgDialog!!.dismiss() }
                    .show()
        }

        if (show) mMsgDialog!!.show()
        else mMsgDialog!!.dismiss()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun dismissDialogs() {
        if (mLoadingDialog != null) {
            mLoadingDialog!!.dismiss()
        }

        if (mMsgDialog != null) {
            mMsgDialog!!.dismiss()
        }
    }
}