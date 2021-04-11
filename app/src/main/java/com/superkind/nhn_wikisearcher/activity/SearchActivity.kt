package com.superkind.nhn_wikisearcher.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.superkind.nhn_wikisearcher.adapter.SearchListAdapter
import com.superkind.nhn_wikisearcher.databinding.ActivitySearchBinding
import com.superkind.nhn_wikisearcher.presenter.WikiSearchPresenter
import com.superkind.nhn_wikisearcher.presenter.WikiSearchPresenterImpl
import com.superkind.nhn_wikisearcher.view.WikiHeaderView
import com.superkind.nhn_wikisearcher.vo.WikiSearchResult

class SearchActivity : AppCompatActivity(), WikiSearchPresenter.View {
    private var mPresenter: WikiSearchPresenter.Presenter = WikiSearchPresenterImpl(this, this)
    private lateinit var mBinding: ActivitySearchBinding
    private lateinit var mListAdapter: SearchListAdapter
    private lateinit var mHeaderView: WikiHeaderView
    private var mLoadingDialog: AlertDialog? = null
    private var mMsgDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySearchBinding.inflate(layoutInflater)

        initView()
        mPresenter.onCreate()

        setContentView(mBinding.root)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
    }

    private fun initView() {
        mBinding.swipeLayout.setOnRefreshListener {
            mPresenter.onSwipeRefresh()
        }

        mBinding.searchBar.setSearchOnClickListener {
            mPresenter.onClickSearch()
        }

        mBinding.searchBar.setBackOnClickListener {
            mPresenter.onClickBack()
        }

        mBinding.searchBar.setEditOnKeyListener { _, keyCode, event -> mPresenter.onKeySearch(keyCode, event) }
    }

    /**
     * EditText의 Text를 가져옵니다.
     */
    override fun getSearchText(): String {
        return mBinding.searchBar.getSearchText()
    }

    private fun dismissDialogs() {
        if (mLoadingDialog != null) {
            mLoadingDialog!!.dismiss()
        }

        if (mMsgDialog != null) {
            mMsgDialog!!.dismiss()
        }
    }

    /**
     * 현재 리스트를 초기화 합니다.
     */
    override fun clearList() {
        mHeaderView.visibility = View.GONE
        mListAdapter.clearList()
    }

    /**
     * 로딩창을 띄웁니다.
     */
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

    override fun getIntentData(): String {
        val search = intent.getStringExtra("search")
        return search?: ""
    }

    override fun initListHeader() {
        mHeaderView = WikiHeaderView(this)
        mHeaderView.visibility = View.GONE
        mBinding.listSearchResult.addHeaderView(mHeaderView)
    }

    /**
     * 헤더 레이아웃을 추가합니다. 이미 있다면 제거하고 추가합니다.
     */
    override fun updateListHeader(result: WikiSearchResult) {
        with(mHeaderView) {
            result.imgDrawable?.let { setImg(it) }
            setImgSize(result.imgWidth, result.imgHeight)
            setTitle(result.displayTitle)
            setExtract(result.extract)
            setOnClickListener()

            visibility = View.VISIBLE
        }

        Log.d("superkind-deug", "updateHeader(): $result")
    }

    /**
     * List Adapter를 초기화 합니다.
     */
    override fun initListAdapter() {
        mListAdapter = SearchListAdapter(this)
        mBinding.listSearchResult.adapter = mListAdapter
    }

    /**
     * 연관 리스트에 데이터를 추가합니다.
     */
    override fun addListItem(item: WikiSearchResult, position: Int) {
        mListAdapter.addData(item)
    }

    /**
     * 연관 리스트에 데이터를 업데이트 합니다.
     */
    override fun setList(list: ArrayList<WikiSearchResult>) {
        mListAdapter.setDataList(list)
    }

    /**
     * 연관 리스트에 데이터를 삭제합니다.
     */
    override fun removeListItem(item: WikiSearchResult, position: Int) {
        mListAdapter.removeItem(item, position)
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun setSearchText(search: String) {
        mBinding.searchBar.setSearchText(search)
        mBinding.searchBar.setSelection(search.length) // 커서를 마지막으로 이동
    }

    override fun setSwipeRefresh(refresh: Boolean) {
        mBinding.swipeLayout.isRefreshing = refresh
    }
}