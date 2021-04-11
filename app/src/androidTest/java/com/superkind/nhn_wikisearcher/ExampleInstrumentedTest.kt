package com.superkind.nhn_wikisearcher

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.superkind.nhn_wikisearcher.model.SearchModel
import com.superkind.nhn_wikisearcher.vo.WikiSearchResult
import com.superkind.restapi.interfaces.HttpCallBack

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.io.InputStream
import java.net.URL

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.superkind.nhn_wikisearcher", appContext.packageName)
    }

    @Test
    fun getSummary() {
        val search = "google"
        SearchModel().requestSummaryData(search, object : SearchModel.WikiSearchListener {
            override fun onSuccess(result: Any) {
                if (result is WikiSearchResult) {
                    Log.d("superkind-debug", result.toString())
                }
            }

            override fun onFailure(code: Int, msg: String) {
                Log.d("superkind-debug", "failed to request, code:$code msg:$msg")
            }
        })
    }

    @Test
    fun getList() {
        val search = "google"
        SearchModel().requestListData(search, object : SearchModel.WikiSearchListener {
            override fun onSuccess(result: Any) {
                val size = (result as ArrayList<*>).size
                Log.d("superkind-debug", "result length: $size")
                Log.d("superkind-debug", result.toString())
            }

            override fun onFailure(code: Int, msg: String) {
                Log.d("superkind-debug", "failed to request, code:$code msg:$msg")
            }

        })
    }


    @Test
    fun getImg() {
        val url = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/30/Building92microsoft.jpg/320px-Building92microsoft.jpg"
        val appContext = InstrumentationRegistry.getInstrumentation().context

        val input = URL(url).content as InputStream
        val d = Drawable.createFromStream(input, "asdaf")
    }

}