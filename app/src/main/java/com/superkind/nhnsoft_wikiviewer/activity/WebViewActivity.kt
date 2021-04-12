package com.superkind.nhnsoft_wikiviewer.activity

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.superkind.nhnsoft_wikiviewer.constant.WikiURL
import com.superkind.nhnsoft_wikiviewer.databinding.ActivityWebviewBinding
import com.superkind.nhnsoft_wikiviewer.presenter.WebViewPresenter
import com.superkind.nhnsoft_wikiviewer.presenter.WebViewPresenterImpl

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

    /**
     * Intent 데이터를 반환합니다.
     */
    override fun getIntentData() : String {
        return intent.getStringExtra("search") ?: ""
    }

    /**
     * 각 View의 Listener 동작을 Presenter에게 위임합니다.
     */
    private fun initView() {
        mBinding.searchBar.setSearchOnClickListener {
            mPresenter.onClickSearch()
        }

        mBinding.searchBar.setBackOnClickListener {
            mPresenter.onClickBack()
        }

        mBinding.searchBar.setEditOnKeyListener {
            _, keyCode, event -> mPresenter.onKeySearch(keyCode, event)
        }
    }

    /**
     * WebView 및 WebViewClient를 셋팅합니다.
     */
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
            builtInZoomControls = true
            displayZoomControls = false
            setSupportZoom(true)
        }
    }

    /**
     * WebView를 시작합니다.
     */
    override fun loadWebView(url: String) {
        if (url.isNotEmpty()) {
            mBinding.webView.loadUrl(WikiURL.detail + url)
        }
    }

    /**
     * EditText의 문자열을 세팅합니다.
     */
    override fun setSearchText(search: String) {
        mBinding.searchBar.setSearchText(search)
        mBinding.searchBar.setSelection(search.length) // 커서를 마지막으로 이동
    }

    /**
     * 현재 EditText의 문자열을 반환합니다.
     */
    override fun getSearchText(): String {
        return mBinding.searchBar.getSearchText()
    }

    /**
     * 로딩창을 생성/표출합니다.
     */
    override fun showLoading(show: Boolean) {
        if (isFinishing) {
            return
        }

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

    /**
     * 메시지 다이얼로그를 생성/표출합니다.
     */
    override fun showMsgDialog(show: Boolean, msg: String) {
        if (isFinishing) {
            return
        }

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

    /**
     * 키보드를 숨깁니다.
     */
    override fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    /**
     * 현재 액티비티의 모든 dialog를 dismiss합니다.
     */
    private fun dismissDialogs() {
        if (isFinishing) {
            return
        }

        if (mLoadingDialog != null) {
            mLoadingDialog!!.dismiss()
        }

        if (mMsgDialog != null) {
            mMsgDialog!!.dismiss()
        }
    }
}