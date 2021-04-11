package com.superkind.nhnsoft_wikiviewer.model

import com.superkind.httpapi.HttpAPI
import com.superkind.httpapi.interfaces.HttpCallBack
import com.superkind.nhnsoft_wikiviewer.constant.WikiURL
import com.superkind.nhnsoft_wikiviewer.extension.tryGet
import com.superkind.nhnsoft_wikiviewer.util.Util
import com.superkind.nhnsoft_wikiviewer.vo.WikiSearchResult
import org.json.JSONObject

class WikiSearchModel {
    interface WikiSearchListener {
        fun onSuccess(result: Any)
        fun onFailure(code: Int, msg: String)
    }

    private val mHttpApi: HttpAPI = HttpAPI()
    private var mLastSearchText: String = ""

    private lateinit var mHeaderData: WikiSearchResult
    private lateinit var mListData: ArrayList<WikiSearchResult>

    /**
     * 요약 정보를 요청합니다.
     */
    fun requestSummaryData(search: String, listener: WikiSearchListener) {
        setLastSearchText(search)

        mHttpApi.get(WikiURL.summary + Util.makeSearchText(search), object : HttpCallBack {
            override fun onFailure(code: Int, msg: String) {
                listener.onFailure(code, msg)
            }

            override fun onSuccess(response: ByteArray) {
                val wikiData = getWikiDataFromJson(String(response))
                listener.onSuccess(wikiData)
            }
        })
    }

    /**
     * 연관 정보를 요청합니다.
     */
    fun requestListData(search: String, listener: WikiSearchListener) {
        setLastSearchText(search)

        mHttpApi.get(WikiURL.related + Util.makeSearchText(search), object : HttpCallBack {
            override fun onFailure(code: Int, msg: String) {
                listener.onFailure(code, msg)
            }

            override fun onSuccess(response: ByteArray) {
                listener.onSuccess(getRelatedItems(String(response)))
            }

        })
    }

    /**
     * 마지막에 검색했던 문자열을 반환합니다.
     */
    fun getLastSearchText() : String {
        return mLastSearchText
    }

    /**
     * 마지막에 검색했던 문자열을 저장합니다.
     */
    private fun setLastSearchText(search: String) {
        mLastSearchText = search
    }

    /**
     * json String을 WikiSearchResult 배열로 변환합니다.
     */
    private fun getRelatedItems(json:String): ArrayList<WikiSearchResult> {
        val jsonObj = JSONObject(json)
        val pages = jsonObj.getJSONArray("pages")

        val resultArr: ArrayList<WikiSearchResult> = arrayListOf()

        for (i in 0 until pages.length()) {
            val jo = pages.getJSONObject(i)
            val wikiData = jsonObjToSearchData(jo)

            resultArr.add(wikiData)
        }
        return resultArr
    }

    /**
     * json String을 WikiSearchResult로 파싱합니다.
     */
    private fun jsonObjToSearchData(jsonObj: JSONObject): WikiSearchResult {
        val thumbnail = jsonObj.tryGet("thumbnail", JSONObject::class.java)
        return if (thumbnail == null) {
             WikiSearchResult(
                jsonObj.tryGet("displaytitle", String::class.java) ?: "",
                jsonObj.tryGet("extract", String::class.java) ?: "",
                "", null, 0, 0)
        } else {
            WikiSearchResult(
                jsonObj.tryGet("displaytitle", String::class.java) ?: "",
                jsonObj.tryGet("extract", String::class.java) ?: "",
                thumbnail.tryGet("source", String::class.java) ?: "",
                null,
                thumbnail.tryGet("width", String::class.java)?.toInt() ?: 0,
                thumbnail.tryGet("height", Int::class.java)?.toInt() ?: 0)
        }
    }

    /**
     * JSON 문자열로부터 Wiki 데이터를 반환합니다.
     */
    private fun getWikiDataFromJson(json: String): WikiSearchResult {
        val jsonObj = JSONObject(json)
        return jsonObjToSearchData(jsonObj)
    }
}