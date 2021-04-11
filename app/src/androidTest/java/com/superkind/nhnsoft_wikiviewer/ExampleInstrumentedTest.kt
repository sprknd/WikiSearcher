package com.superkind.nhnsoft_wikiviewer

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.superkind.nhnsoft_wikiviewer.model.WikiSearchModel
import com.superkind.nhnsoft_wikiviewer.vo.WikiSearchResult

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
        assertEquals("com.superkind.nhnsoft_wikiviewer", appContext.packageName)
    }

    @Test
    fun getSummary() {
        val search = "google"
        WikiSearchModel().requestSummaryData(search, object : WikiSearchModel.WikiSearchListener {
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
    fun getRelatedList() {
        val search = "google"
        WikiSearchModel().requestListData(search, object : WikiSearchModel.WikiSearchListener {
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
    fun getImgDrawble() {
        val url = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/30/Building92microsoft.jpg/320px-Building92microsoft.jpg"
        val appContext = InstrumentationRegistry.getInstrumentation().context

        val input = URL(url).content as InputStream
        val d = Drawable.createFromStream(input, "asdaf")

        assertNotNull(d)
    }

}