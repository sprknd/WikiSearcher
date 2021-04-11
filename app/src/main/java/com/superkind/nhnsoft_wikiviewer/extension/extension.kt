package com.superkind.nhnsoft_wikiviewer.extension

import org.json.JSONArray
import org.json.JSONObject

/**
 * JsonObject Extension
 * 지정한 타입으로 파싱합니다.
 */
fun <T> JSONObject.tryGet(key: String, type: Class<T>): T? {
    return try {
        when (type) {
            JSONObject::class.java -> getJSONObject(key) as T
            JSONArray::class.java -> getJSONArray(key) as T
            String::class.java -> getString(key) as T
            Boolean::class.java -> getBoolean(key) as T
            Int::class.java -> getInt(key) as T
            Double::class.java -> getDouble(key) as T else -> null
        }
    } catch (e: Exception) {
        null
    }
}