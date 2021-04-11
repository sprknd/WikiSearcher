package com.superkind.nhn_wikisearcher.model

import com.superkind.nhn_wikisearcher.constant.WikiURL
import com.superkind.nhn_wikisearcher.extension.tryGet
import com.superkind.nhn_wikisearcher.vo.WikiSearchResult
import com.superkind.restapi.HttpAPI
import com.superkind.restapi.interfaces.HttpCallBack
import org.json.JSONObject

class SearchModel {
    interface WikiSearchListener {
        fun onSuccess(result: Any)
        fun onFailure(code: Int, msg: String)
    }

    private val mHttpApi: HttpAPI = HttpAPI()

    private lateinit var mHeaderData: WikiSearchResult
    private lateinit var mListData: ArrayList<WikiSearchResult>

    /**
     * 요약 정보를 요청합니다.
     */
    fun requestSummaryData(search: String, listener: WikiSearchListener) {
        mHttpApi.get(WikiURL.summary + makeSearchText(search), object : HttpCallBack {
            override fun onFailure(code: Int, msg: String) {
                listener.onFailure(code, msg)
            }

            override fun onSuccess(response: ByteArray) {
                listener.onSuccess(getWikiDataFromJson(String(response)))
            }
        })
    }

    /**
     * 연관 정보를 요청합니다.
     */
    fun requestListData(search: String, listener: WikiSearchListener) {
        mHttpApi.get(WikiURL.related + makeSearchText(search), object : HttpCallBack {
            override fun onFailure(code: Int, msg: String) {
                listener.onFailure(code, msg)
            }

            override fun onSuccess(response: ByteArray) {
                listener.onSuccess(getRelatedItems(String(response)))
            }

        })
    }

    /**
     * json String을 WikiSearchResult 배열로 변환합니다.
     */
    private fun getRelatedItems(json:String): ArrayList<WikiSearchResult> {
        val jsonObj = JSONObject(json)
        val pages = jsonObj.getJSONArray("pages")

        val resultArr: ArrayList<WikiSearchResult> = arrayListOf()

        for (i in 0 until pages.length()) {
            val json = pages.getJSONObject(i)
            resultArr.add(jsonObjToSearchData(json))
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

    private fun getWikiDataFromJson(json: String): WikiSearchResult {
        val jsonObj = JSONObject(json)
        return jsonObjToSearchData(jsonObj)
    }

    private fun makeSearchText(search: String) : String {
        return search.replace(" ", "_")
                .replace("<i>", "")
                .replace("</i>", "")
    }
}