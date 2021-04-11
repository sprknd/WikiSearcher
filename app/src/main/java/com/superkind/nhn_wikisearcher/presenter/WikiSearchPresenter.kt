package com.superkind.nhn_wikisearcher.presenter

import com.superkind.nhn_wikisearcher.vo.WikiSearchResult

interface WikiSearchPresenter {
    interface View {
        fun updateHeader(result: WikiSearchResult)
        fun addItem(item: WikiSearchResult, position: Int)
        fun updateList(list: ArrayList<WikiSearchResult>)
        fun removeItem(item: WikiSearchResult, position: Int)
        fun clearList()
        fun showLoading(show: Boolean)
        fun showLoading(show: Boolean, msg: String)
    }

    interface Presenter {
        fun getListData(search: String)
    }
}