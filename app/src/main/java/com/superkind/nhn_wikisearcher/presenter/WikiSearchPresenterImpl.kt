package com.superkind.nhn_wikisearcher.presenter

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.superkind.nhn_wikisearcher.model.SearchModel
import com.superkind.nhn_wikisearcher.vo.WikiSearchResult
import java.io.InputStream
import java.lang.Exception
import java.net.URL

class WikiSearchPresenterImpl(c: Context, view: WikiSearchPresenter.View) : WikiSearchPresenter.Presenter {
    var mView = view
    var mModel = SearchModel()
    val context = c

    /**
     * 모델에 검색 데이터를 요청합니다.
     */
    override fun getListData(search: String) {
        if (search.isEmpty()) {
            return
        }

        mView.showLoading(true) // 로딩 ON
        mView.clearList() // 리스트 초기화

        Thread {
            mModel.requestSummaryData(search, object : SearchModel.WikiSearchListener {
                override fun onSuccess(result: Any) {
                    if (result is WikiSearchResult) {
                        result.img = getImg(result.imgSrc, result.displayTitle)

                        doOnUiThread {
                            mView.updateHeader(result)
                            mView.showLoading(false) // 로딩 OFF
                        }
                    }
                }

                override fun onFailure(code: Int, msg: String) {
                    Log.d("superkind-debug", "requestListData onFailure! code: $code msg: $msg")
//                    doOnUiThread {
//                        mView.clearList()
//                        mView.showLoading(true, msg)
//                    }
                }

            })

            mModel.requestListData(search, object : SearchModel.WikiSearchListener {
                override fun onSuccess(result: Any) {
                    if (result is ArrayList<*>) {
                        val list: ArrayList<WikiSearchResult> = arrayListOf()
                        for (i in 0 until result.size) {
                            (result[i] as WikiSearchResult).img = getImg(
                                (result[i] as WikiSearchResult).imgSrc,
                                (result[i] as WikiSearchResult).displayTitle)

                            list.add(result[i] as WikiSearchResult)

                            doOnUiThread {
                                mView.addItem(result[i] as WikiSearchResult, i)
                            }

                            // 중간쯤 부터 로딩창 해제
                            if (i == result.size / 2) {
                                mView.showLoading(false)
                            }
                        }

                        doOnUiThread {
                            mView.showLoading(false)
                        }
                    }
                }

                override fun onFailure(code: Int, msg: String) {
                    Log.d("superkind-debug", "requestListData onFailure! code: $code msg: $msg")
                    (context as Activity).runOnUiThread {
                        mView.clearList()
                        mView.showLoading(true, msg)
                    }
                }
            })

        }.start()
    }



    fun getImg(url: String, srcName: String): Drawable? {
        return try {
            Log.d("superkind-debug", "image url: $url")
            val input: InputStream
            if (url.isNotEmpty()) {
                input = URL(url).content as InputStream
                Drawable.createFromStream(input, "drawable_img_${srcName}")
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun doOnUiThread(r: Runnable) {
        if (context is Activity) {
            context.runOnUiThread(r)
        }
    }
}