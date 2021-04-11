package com.superkind.nhnsoft_wikiviewer.presenter

import android.view.KeyEvent
import com.superkind.nhnsoft_wikiviewer.vo.WikiSearchResult

interface WikiSearchPresenter {
    interface View {
        // List
        fun initListHeader()
        fun updateListHeader(result: WikiSearchResult)
        fun initListAdapter()
        fun clearList()
        fun addListItem(item: WikiSearchResult, position: Int)
        fun setList(list: ArrayList<WikiSearchResult>)
        fun removeListItem(item: WikiSearchResult, position: Int)

        // Action
        fun goBack()
        fun hideKeyboard()

        // Layout
        fun getSearchText(): String
        fun setSearchText(search: String)
        fun setSwipeRefresh(refresh: Boolean)

        // Loading
        fun showLoading(show: Boolean)
        fun showMsgDialog(show: Boolean, msg: String)

        // Activity
        fun getIntentData() : String
    }

    interface Presenter {
        // View Life Cycle
        fun onCreate()
        fun onResume()

        // Action
        fun onSwipeRefresh()
        fun onClickSearch()
        fun onClickBack()
        fun onKeySearch(keyCode: Int, event: KeyEvent?): Boolean

        // List
        fun requestListData(search: String)
    }
}