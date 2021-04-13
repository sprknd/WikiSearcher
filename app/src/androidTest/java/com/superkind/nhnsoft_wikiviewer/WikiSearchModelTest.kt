package com.superkind.nhnsoft_wikiviewer

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.superkind.httpapi.interfaces.HttpCallBack
import com.superkind.nhnsoft_wikiviewer.model.WikiSearchModel
import com.superkind.nhnsoft_wikiviewer.presenter.WikiSearchPresenter
import com.superkind.nhnsoft_wikiviewer.presenter.WikiSearchPresenterImpl
import com.superkind.nhnsoft_wikiviewer.vo.WikiSearchResult
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class WikiSearchModelTest {
    @Test
    fun requestListData() {
        val appContext = InstrumentationRegistry.getInstrumentation().context
        val model = WikiSearchModel()

        model.requestListData("google", object : WikiSearchModel.WikiSearchListener {
            override fun onSuccess(result: Any) {
                if (result is ArrayList<*>) {
                    Assert.assertTrue(true)
                } else {
                    Assert.fail()
                }
            }

            override fun onFailure(code: Int, msg: String) {
                Assert.fail()
            }
        })
    }

    @Test
    fun requestSummaryData() {
        val model = WikiSearchModel()

        model.requestSummaryData("google", object : WikiSearchModel.WikiSearchListener {
            override fun onSuccess(result: Any) {
                if (result is WikiSearchResult) {
                    Assert.assertTrue(true)
                } else {
                    Assert.fail()
                }
            }

            override fun onFailure(code: Int, msg: String) {
                Assert.fail()
            }
        })
    }
}