package com.superkind.nhn_wikisearcher.activity

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.superkind.nhn_wikisearcher.adapter.SearchListAdapter
//import com.superkind.nhn_wikisearcher.R
//import com.superkind.nhn_wikisearcher.adapter.SearchListAdapter
import com.superkind.nhn_wikisearcher.databinding.ActivityMainBinding
import com.superkind.nhn_wikisearcher.presenter.WikiSearchPresenter
import com.superkind.nhn_wikisearcher.presenter.WikiSearchPresenterImpl
import com.superkind.nhn_wikisearcher.view.WikiHeaderView
import com.superkind.nhn_wikisearcher.vo.WikiSearchResult

class MainActivity : AppCompatActivity(), WikiSearchPresenter.View {
    var presenter: WikiSearchPresenter.Presenter = WikiSearchPresenterImpl(this, this)
    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: SearchListAdapter
    private var headerView: View? = null
    private var loadingDialog: AlertDialog? = null
    private var msgDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        initView()

        setContentView(binding.root)
    }

    private fun initView() {
        // 당겨서 새로고침 합니다.
        binding.swipeLayout.setOnRefreshListener {
            requestSearch()
            binding.swipeLayout.isRefreshing = false
        }

        binding.imgBtnSearch.setOnClickListener {
            requestSearch()
        }

        binding.imgBtnBack.setOnClickListener {
            onBackPressed()
        }

        binding.txSearch.setOnKeyListener(object: View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    presenter.getListData(getSearchText())
                    return true
                }
                return false
            }

        })
    }

    /**
     * EditText의 Text를 가져옵니다.
     */
    private fun getSearchText(): String {
        return binding.txSearch.text.toString()
    }

    /**
     * Presenter에게 EditText에 입력되어 있는 문구로 검색 데이터를 요청합니다.
     */
    private fun requestSearch() {
        val search = getSearchText()
        if (search.isNotEmpty()) {
            presenter.getListData(search)
        }
    }

    private fun dismissDialogs() {
        if (loadingDialog != null) {
            loadingDialog!!.dismiss()
        }

        if (msgDialog != null) {
            msgDialog!!.dismiss()
        }
    }

    /**
     * 현재 리스트를 초기화 합니다.
     */
    override fun clearList() {
        if (headerView != null) {
            binding.listSearchResult.removeHeaderView(headerView)
        }
        listAdapter.clearList()
    }

    /**
     * 로딩창을 띄웁니다.
     */
    override fun showLoading(show: Boolean) {
        dismissDialogs()

        if (loadingDialog == null) {
            loadingDialog = AlertDialog.Builder(this)
                .setMessage("Please wait a second..")
                .create()
        }

        if (show) loadingDialog!!.show()
        else loadingDialog!!.dismiss()
    }

    override fun showLoading(show: Boolean, msg: String) {
        dismissDialogs()

        if (msgDialog == null) {
            msgDialog = AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(msg)
                .setPositiveButton("OK") { _, _ -> msgDialog!!.dismiss() }
                .show()
        }

        if (show) msgDialog!!.show()
        else msgDialog!!.dismiss()
    }

    /**
     * List Adapter를 초기화 합니다.
     */
    private fun initAdapter() {
        listAdapter = SearchListAdapter(this)
        binding.listSearchResult.adapter = listAdapter
    }

    /**
     * 헤더 레이아웃을 추가합니다. 이미 있다면 제거하고 추가합니다.
     */
    override fun updateHeader(result: WikiSearchResult) {
        headerView = WikiHeaderView(this)

        with(headerView as WikiHeaderView) {
            result.img?.let { setImg(it) }
            setImgSize(result.imgWidth, result.imgHeight)
            setTitle(result.displayTitle)
            setExtract(result.extract)
        }

        Log.d("superkind-deug", "updateHeader(): $result")

        binding.listSearchResult.removeHeaderView(headerView)
        binding.listSearchResult.addHeaderView(headerView)

    }

    /**
     * 연관 리스트에 데이터를 추가합니다.
     */
    override fun addItem(item: WikiSearchResult, position: Int) {
        listAdapter.addData(item)
    }

    /**
     * 연관 리스트에 데이터를 업데이트 합니다.
     */
    override fun updateList(list: ArrayList<WikiSearchResult>) {
        listAdapter.setDataList(list)
    }

    /**
     * 연관 리스트에 데이터를 삭제합니다.
     */
    override fun removeItem(item: WikiSearchResult, position: Int) {
        listAdapter.removeItem(item, position)
    }
}