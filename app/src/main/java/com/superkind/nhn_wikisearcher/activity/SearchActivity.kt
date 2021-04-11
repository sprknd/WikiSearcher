package com.superkind.nhn_wikisearcher.activity

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.security.ProviderInstaller
import com.superkind.nhn_wikisearcher.adapter.SearchListAdapter
import com.superkind.nhn_wikisearcher.databinding.ActivityMainBinding
import com.superkind.nhn_wikisearcher.presenter.WikiSearchPresenter
import com.superkind.nhn_wikisearcher.presenter.WikiSearchPresenterImpl
import com.superkind.nhn_wikisearcher.view.WikiHeaderView
import com.superkind.nhn_wikisearcher.vo.WikiSearchResult

class SearchActivity : AppCompatActivity(), WikiSearchPresenter.View {
    var presenter: WikiSearchPresenter.Presenter = WikiSearchPresenterImpl(this, this)
    private lateinit var binding: ActivityMainBinding
    private lateinit var listAdapter: SearchListAdapter
    private lateinit var headerView: WikiHeaderView
    private var loadingDialog: AlertDialog? = null
    private var msgDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // android versions < 4.4 bug fix
        ProviderInstaller.installIfNeeded(applicationContext)

        binding = ActivityMainBinding.inflate(layoutInflater)

        initView()

        /**
         * get Intent data
         */
        val search = intent.getStringExtra("search")
        presenter.getListData(search?: "") // Intent로 들어온 값으로 검색을 시도합니다
        binding.txSearch.setText(search?: "") // Intent로 들어온 값을 EditText에 지정합니다
        binding.txSearch.setSelection(search?.length?: 0)

        setContentView(binding.root)
    }

    private fun initView() {
        initHeader()
        initAdapter()

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
     * List Adapter를 초기화 합니다.
     */
    private fun initAdapter() {
        listAdapter = SearchListAdapter(this)
        binding.listSearchResult.adapter = listAdapter
    }

    private fun initHeader() {
        headerView = WikiHeaderView(this)
        headerView.visibility = View.GONE
        binding.listSearchResult.addHeaderView(headerView)
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
        headerView.visibility = View.GONE
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
     * 헤더 레이아웃을 추가합니다. 이미 있다면 제거하고 추가합니다.
     */
    override fun updateHeader(result: WikiSearchResult) {
        with(headerView) {
            result.img?.let { setImg(it) }
            setImgSize(result.imgWidth, result.imgHeight)
            setTitle(result.displayTitle)
            setExtract(result.extract)
            setOnClickListener()

            visibility = View.VISIBLE
        }

        Log.d("superkind-deug", "updateHeader(): $result")
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