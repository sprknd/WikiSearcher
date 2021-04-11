package com.superkind.nhnsoft_wikiviewer.util

import android.graphics.drawable.Drawable
import android.util.Log
import java.io.InputStream
import java.lang.Exception
import java.net.URL

object Util {
    /**
     * 데이터 표출시 유효한 문자열을 반환합니다.
     */
    fun makeDisplayText(search: String): String {
        return search.replace("<i>", "")
            .replace("</i>", "")
            .trim()
    }

    /**
     * 검색시 유효한 문자열을 반환합니다.
     */
    fun makeSearchText(search: String): String {
        return makeDisplayText(search)
            .replace(" ", "_")
    }

    /**
     * URL로부터 이미지를 Drawable로 반환합니다.
     */
    fun getImgDrawable(url: String, srcName: String): Drawable? {
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
}