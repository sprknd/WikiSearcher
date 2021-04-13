package com.superkind.nhnsoft_wikiviewer.activity

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.superkind.nhnsoft_wikiviewer.databinding.ActivitySearchBinding
import com.superkind.nhnsoft_wikiviewer.presenter.WikiSearchPresenter
import com.superkind.nhnsoft_wikiviewer.presenter.WikiSearchPresenterImpl
import com.superkind.nhnsoft_wikiviewer.util.EspressoIdleResource
import com.superkind.nhnsoft_wikiviewer.vo.WikiSearchResult

class WikiSearchActivity : AppCompatActivity(), WikiSearchPresenter.View {
    var mPresenter: WikiSearchPresenter.Presenter = WikiSearchPresenterImpl(this, this)
    private lateinit var mBinding: ActivitySearchBinding

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

    /**
     * 각 View의 Listener 동작을 Presenter에게 위임합니다.
     */
    private fun initView() {
        mBinding.mainListWiki.setOnRefreshListener {
            mPresenter.onSwipeRefresh()
        }

        mBinding.mainSearchBar.setSearchOnClickListener {
            mPresenter.onClickSearch()
        }

        mBinding.mainSearchBar.setBackOnClickListener {
            mPresenter.onClickBack()
        }

        mBinding.mainSearchBar.setEditOnKeyListener {
            _, keyCode, event -> mPresenter.onKeySearch(keyCode, event)
        }
    }

    /**
     * EditText의 Text를 가져옵니다.
     */
    override fun getSearchText(): String {
        return mBinding.mainSearchBar.getSearchText()
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

    /**
     * 현재 리스트를 초기화 합니다.
     */
    override fun clearList() {
        mBinding.mainListWiki.clearList()
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

        if (show) {
            mLoadingDialog!!.show()
        }
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

    /**
     * Intent 데이터를 반환합니다.
     */
    override fun getIntentData(): String {
        val search = intent.getStringExtra("search")
        return search?: ""
    }

    override fun initListHeader() {
        mBinding.mainListWiki.initListHeader()
    }

    /**
     * 헤더 레이아웃을 추가합니다. 이미 있다면 제거하고 추가합니다.
     */
    override fun updateListHeader(result: WikiSearchResult) {
        mBinding.mainListWiki.updateListHeader(result)
    }

    /**
     * List Adapter를 초기화 합니다.
     */
    override fun initListAdapter() {
        mBinding.mainListWiki.initListAdapter()
    }

    /**
     * 연관 리스트에 데이터를 추가합니다.
     */
    override fun addListItem(item: WikiSearchResult, position: Int) {
        mBinding.mainListWiki.addListItem(item, position)
    }

    /**
     * 연관 리스트에 데이터를 업데이트 합니다.
     */
    override fun setList(list: ArrayList<WikiSearchResult>) {
        mBinding.mainListWiki.setList(list)
    }

    /**
     * 연관 리스트에 데이터를 삭제합니다.
     */
    override fun removeListItem(item: WikiSearchResult, position: Int) {
        mBinding.mainListWiki.removeListItem(item, position)
    }

    /**
     * 리스트에 추가 작업이 끝나면 호출됩니다.
     */
    override fun addListItemDone() {
        EspressoIdleResource.decrement() // Espresso done
    }

    override fun goBack() {
        onBackPressed()
    }

    /**
     * 가상 키보드를 숨김니다.
     */
    override fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    /**
     * EditText에 텍스트를 세팅합니다.
     */
    override fun setSearchText(search: String) {
        mBinding.mainSearchBar.setSearchText(search)
        mBinding.mainSearchBar.setSelection(search.length) // 커서를 마지막으로 이동
    }

    /**
     * Swipe Refresh를 설정합니다.
     */
    override fun setSwipeRefresh(refresh: Boolean) {
        mBinding.mainListWiki.setSwipeRefresh(refresh)
    }
}