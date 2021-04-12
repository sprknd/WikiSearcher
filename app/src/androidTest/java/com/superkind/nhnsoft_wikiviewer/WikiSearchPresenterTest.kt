package com.superkind.nhnsoft_wikiviewer

import android.support.test.InstrumentationRegistry
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.superkind.nhnsoft_wikiviewer.presenter.WikiSearchPresenter
import com.superkind.nhnsoft_wikiviewer.presenter.WikiSearchPresenterImpl
import com.superkind.nhnsoft_wikiviewer.vo.WikiSearchResult
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WikiSearchPresenterTest {
    val view = VirtualView()
    lateinit var presenter: WikiSearchPresenterImpl

    @Before
    fun init() {
        presenter = WikiSearchPresenterImpl(
            ApplicationProvider.getApplicationContext(), view)
    }

    @Test()
    fun search() {
        presenter.requestListData("microsoft")
        Thread.sleep(5000)
    }
}

class VirtualView(): WikiSearchPresenter.View {
    override fun initListHeader() {
    }

    override fun updateListHeader(result: WikiSearchResult) {
    }

    override fun initListAdapter() {
    }

    override fun clearList() {
    }

    override fun addListItem(item: WikiSearchResult, position: Int) {
        Assert.assertTrue(true)
    }

    override fun setList(list: ArrayList<WikiSearchResult>) {
    }

    override fun removeListItem(item: WikiSearchResult, position: Int) {
    }

    override fun addListItemDone() {
    }

    override fun goBack() {
    }

    override fun hideKeyboard() {
    }

    override fun getSearchText(): String {
        return ""
    }

    override fun setSearchText(search: String) {
    }

    override fun setSwipeRefresh(refresh: Boolean) {
    }

    override fun showLoading(show: Boolean) {
    }

    override fun showMsgDialog(show: Boolean, msg: String) {
    }

    override fun getIntentData(): String {
        return ""
    }

}