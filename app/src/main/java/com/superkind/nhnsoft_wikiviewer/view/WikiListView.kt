package com.superkind.nhnsoft_wikiviewer.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.superkind.nhnsoft_wikiviewer.adapter.SearchListAdapter
import com.superkind.nhnsoft_wikiviewer.databinding.ListWikiBinding
import com.superkind.nhnsoft_wikiviewer.vo.WikiSearchResult

class WikiListView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private val mBinding = ListWikiBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var mHeaderView: WikiHeaderView
    private lateinit var mListAdapter: SearchListAdapter


    fun initListHeader() {
        mHeaderView = WikiHeaderView(context)
        mHeaderView.visibility = View.GONE
        mBinding.listSearchResult.addHeaderView(mHeaderView)
    }

    fun updateListHeader(result: WikiSearchResult) {
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

    fun getListHeader(): WikiHeaderView {
        return mHeaderView
    }

    fun initListAdapter() {
        mListAdapter = SearchListAdapter(context)
        mBinding.listSearchResult.adapter = mListAdapter
    }

    fun addListItem(item: WikiSearchResult, position: Int) {
        mListAdapter.addData(item)
    }

    fun setList(list: ArrayList<WikiSearchResult>) {
        mListAdapter.setDataList(list)
    }

    fun removeListItem(item: WikiSearchResult, position: Int) {
        mListAdapter.removeItem(item, position)
    }

    fun setOnRefreshListener(listener: SwipeRefreshLayout.OnRefreshListener) {
        mBinding.swipeLayout.setOnRefreshListener(listener)
    }

    fun setSwipeRefresh(refresh: Boolean) {
        mBinding.swipeLayout.isRefreshing = refresh
    }

    fun clearList() {
        mHeaderView.visibility = View.GONE
        mListAdapter.clearList()
    }
}