package com.superkind.nhn_wikisearcher.presenter

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.KeyEvent
import com.google.android.gms.security.ProviderInstaller
import com.superkind.nhn_wikisearcher.model.WikiSearchModel
import com.superkind.nhn_wikisearcher.util.Util
import com.superkind.nhn_wikisearcher.vo.WikiSearchResult
import java.io.InputStream
import java.lang.Exception
import java.net.URL

class WikiSearchPresenterImpl(c: Context, view: WikiSearchPresenter.View) : WikiSearchPresenter.Presenter {
    private var mModel = WikiSearchModel()
    private var mView = view
    private val mContext = c

    // Activity LifeCycle
    override fun onCreate() {
        // android versions < 4.4 bug fix
        ProviderInstaller.installIfNeeded(mContext.applicationContext)

        mView.initListHeader() // init header first
        mView.initListAdapter() // then init adapter

        // Intent로 들어온 값으로 검색을 시도합니다
        val str = mView.getIntentData()
        if (str.isNotEmpty()) {
            requestListData(str)
            mView.setSearchText(str) // Intent로 들어온 값을 EditText에 지정합니다
        }
    }

    // Activity LifeCycle
    override fun onResume() {
    }

    // Activity Action
    override fun onSwipeRefresh() {
        val search = Util.makeSearchText(mView.getSearchText())
        requestListData(search)

        mView.setSwipeRefresh(false)
    }

    // Activity Action
    override fun onClickSearch() {
        val search = Util.makeSearchText(mView.getSearchText())
        requestListData(search)
    }

    // Activity Action
    override fun onClickBack() {
        mView.goBack()
    }

    // Activity Action
    override fun onKeySearch(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
            requestListData(mView.getSearchText())
            return true
        }
        return false
    }

    /**
     * 모델에 검색 데이터를 요청합니다.
     */
    override fun requestListData(search: String) {
        if (search.isEmpty()) {
            return
        }

        mView.showLoading(true) // 로딩 ON
        mView.clearList() // 리스트 초기화

        Thread {
            mModel.requestSummaryData(search, object : WikiSearchModel.WikiSearchListener {
                override fun onSuccess(result: Any) {
                    if (result is WikiSearchResult) {
                        with(result) {
                            displayTitle = Util.makeDisplayText(displayTitle)
                            imgDrawable = getImgDrawable(imgSrc, displayTitle)
                        }

                        doOnUiThread {
                            mView.updateListHeader(result)
                            mView.hideKeyboard()
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

            mModel.requestListData(search, object : WikiSearchModel.WikiSearchListener {
                override fun onSuccess(result: Any) {
                    if (result is ArrayList<*>) {
                        val list: ArrayList<WikiSearchResult> = arrayListOf()
                        for (i in 0 until result.size) {
                            with(result[i] as WikiSearchResult) {
                                displayTitle = Util.makeDisplayText(displayTitle)
                                imgDrawable = getImgDrawable(imgSrc, displayTitle)

                            }

                            list.add(result[i] as WikiSearchResult)

                            // 데이터가 가공될때마다 리스트에 추가합니다.
                            doOnUiThread {
                                mView.addListItem(result[i] as WikiSearchResult, i)
                            }

                            // 리스트에 하나 추가되면 로딩 제거
                            if (i > 0) {
                                doOnUiThread {
                                    mView.showLoading(false)
                                    mView.hideKeyboard()
                                }
                            }
                        }
                        doOnUiThread {
                            mView.showLoading(false)
                        }
                    }
                }

                override fun onFailure(code: Int, msg: String) {
                    Log.d("superkind-debug", "requestListData onFailure! code: $code msg: $msg")
                    (mContext as Activity).runOnUiThread {
                        mView.clearList()
                        mView.showMsgDialog(true, msg)
                    }
                }
            })

        }.start()
    }

    // URL로부터 이미지를 Drawable로 반환합니다.
    private fun getImgDrawable(url: String, srcName: String): Drawable? {
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
        if (mContext is Activity) {
            mContext.runOnUiThread(r)
        }
    }
}