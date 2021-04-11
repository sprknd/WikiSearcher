package com.superkind.nhnsoft_wikiviewer.presenter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.KeyEvent
import com.google.android.gms.security.ProviderInstaller
import com.superkind.nhnsoft_wikiviewer.model.WikiSearchModel
import com.superkind.nhnsoft_wikiviewer.util.Util
import com.superkind.nhnsoft_wikiviewer.vo.WikiSearchResult

class WikiSearchPresenterImpl(context: Context, view: WikiSearchPresenter.View) : WikiSearchPresenter.Presenter {
    private var mModel = WikiSearchModel()
    private var mView = view
    private val mContext = context

    private var mIsInitialSearch = true

    private fun initList() {
        if (mIsInitialSearch) {
            mIsInitialSearch = false

            // Adapter를 set하기 전에 header/footer를 추가해야 함
            mView.initListHeader()
            mView.initListAdapter()
        }
    }

    // Activity LifeCycle
    override fun onCreate() {
        // android versions < 4.4 bug fix
        ProviderInstaller.installIfNeeded(mContext.applicationContext)

        // Adapter를 set하기 전에 header/footer를 추가해야 함
//        mView.initListHeader()
//        mView.initListAdapter()

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

    /**
     * Swipe Refresh, 마지막에 요청했던 String으로 요청합니다.
     */
    override fun onSwipeRefresh() {
        val search = Util.makeSearchText(mModel.getLastSearchText()) // Model에서 가져옴
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

    /**
     * EditText 입력마다 호출합니다.
     * Enter 키 동작 시 리스트 데이터를 요청합니다.
     */
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

        initList()

        mView.showLoading(true) // 로딩 ON
        mView.clearList() // 리스트 초기화

        Thread {
            mModel.requestSummaryData(search, object : WikiSearchModel.WikiSearchListener {
                override fun onSuccess(result: Any) {
                    if (result is WikiSearchResult) {
                        // 문자열, 이미지를 가공하여 저장합니다.
                        with(result) {
                            displayTitle = Util.makeDisplayText(displayTitle)
                            imgDrawable = Util.getImgDrawable(imgSrc, displayTitle)
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
        }.start()

        Thread {
            mModel.requestListData(search, object : WikiSearchModel.WikiSearchListener {
                override fun onSuccess(result: Any) {
                    if (result is ArrayList<*>) {
                        for (i in 0 until result.size) {
                            val wikiData = result[i] as WikiSearchResult

                            // 문자열, 이미지를 가공하여 저장합니다.
                            with(wikiData) {
                                displayTitle = Util.makeDisplayText(displayTitle)
                                imgDrawable = Util.getImgDrawable(imgSrc, displayTitle)
                            }

                            // 리스트에 추가합니다.
                            doOnUiThread {
                                mView.addListItem(result[i] as WikiSearchResult, i)

                                // 연관 리스트에 하나 추가되면 로딩창을 제거합니다.
                                mView.showLoading(false)
                                mView.hideKeyboard()
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

    /**
     * UI스레드에서 작업합니다.
     */
    private fun doOnUiThread(r: Runnable) {
        if (mContext is Activity) {
            mContext.runOnUiThread(r)
        }
    }
}