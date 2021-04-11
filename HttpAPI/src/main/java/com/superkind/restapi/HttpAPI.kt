package com.superkind.restapi

import android.content.ContentValues
import com.superkind.restapi.interfaces.HttpCallBack
import com.superkind.restapi.types.HttpType
import com.superkind.restapi.types.MethodType
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection

class HttpAPI {
    private var mReqProperty: MutableMap<String, String> = mutableMapOf()
    private var mTimeout: Int = 15000

    /**
     * GET 메서드 요청
     */
    fun get(url: String, callBack: HttpCallBack) {
        var conn: HttpURLConnection? = null
        try {
            conn = getHttpUrlConnection(url, MethodType.GET)
            if (conn == null) {
                callBack.onFailure(-1, "not supported url")
                return
            }

            with(conn) {
                when (responseCode) {
                    HttpURLConnection.HTTP_OK -> {
                        callBack.onSuccess(inputStream.readBytes())
                        inputStream.close()
                    }
                    else -> callBack.onFailure(responseCode, responseMessage)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            e.message?.let { callBack.onFailure(-1, it) }
        } finally {
            conn?.run {
                disconnect()
            }
        }
    }

    /**
     * POST 메서드 요청
     */
    fun post(url: String, body: ByteArray, callBack: HttpCallBack) {
        var conn: HttpURLConnection? = null
        try {
            conn = getHttpUrlConnection(url, MethodType.POST)
            if (conn == null) {
                callBack.onFailure(-1, "not valid url")
                return
            }

            with(conn) {
                doOutput = true

                val outputWriter = DataOutputStream(outputStream)
                outputWriter.write(body)
                outputWriter.flush()
                outputWriter.close()

                when (responseCode) {
                    HttpURLConnection.HTTP_OK -> {
                        callBack.onSuccess(inputStream.readBytes())
                        inputStream.close()
                    }
                    else -> callBack.onFailure(responseCode, responseMessage)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callBack.onFailure(-1, e.printStackTrace().toString())
        } finally {
            conn?.run {
                disconnect()
            }
        }
    }

    /**
     * PUT 메서드 요청
     */
    fun put(url: String, content: ByteArray, callBack: HttpCallBack) {
        var conn: HttpURLConnection? = null
        try {
            // HttpUrlConnection 설정
            conn = getHttpUrlConnection(url, MethodType.PUT)
            if (conn == null) {
                callBack.onFailure(-1, "not valid url")
                return
            }

            with(conn) {
                doOutput = true

                // 데이터 전달
                val outputWriter = DataOutputStream(outputStream)
                outputWriter.write(content)
                outputWriter.flush()
                outputWriter.close()

                // 연결 요청 확인
                when (responseCode) {
                    HttpURLConnection.HTTP_OK -> {
                        callBack.onSuccess(inputStream.readBytes())
                        inputStream.close()
                    }
                    else -> callBack.onFailure(responseCode, responseMessage)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callBack.onFailure(-1, e.printStackTrace().toString())
        } finally {
            conn?.run {
                inputStream.close()
                disconnect()
            }
        }
    }

    /**
     * DELETE 메서드 요청
     */
    fun delete(url: String, callBack: HttpCallBack) {
        var conn: HttpURLConnection? = null
        try {
            conn = getHttpUrlConnection(url, MethodType.DELETE)
            if (conn == null) {
                callBack.onFailure(-1, "not valid url")
                return
            }

            with(conn) {
                when (responseCode) {
                    HttpURLConnection.HTTP_OK -> {
                        callBack.onSuccess(inputStream.readBytes())
                        inputStream.close()
                    }
                    else -> callBack.onFailure(responseCode, responseMessage)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callBack.onFailure(-1, e.printStackTrace().toString())
        } finally {
            conn?.run {
                inputStream.close()
                disconnect()
            }
        }
    }

    /**
     * Request Header 값을 설정합니다.
     */
    fun setRequestHeader(key: String, value: String) {
        mReqProperty[key] = value
    }

    /**
     * API 접속 timeout 시간을 설정합니다.
     */
    fun setTimeout(timeout: Int) {
        if (timeout > 0) {
            mTimeout = timeout
        }
    }

    private fun checkUrl(url: String) : HttpType {
        return when {
            url.startsWith("https://") -> {
                HttpType.HTTPS
            }
            url.startsWith("http://") -> {
                HttpType.HTTP
            }
            else -> {
                HttpType.UNKNOWN
            }
        }
    }

    private fun getHttpUrlConnection(urlString: String, method: MethodType) : HttpURLConnection? {
        val httpType = checkUrl(urlString)
        val url = URL(urlString)

        return when(httpType) {
            HttpType.HTTP -> {
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = method.toString()
                conn.connectTimeout = mTimeout
                setHeaders(conn)
                conn
            }
            HttpType.HTTPS -> {
                val conn = url.openConnection() as HttpsURLConnection
                conn.requestMethod = method.toString()
                conn.connectTimeout = mTimeout
                setHeaders(conn)
                conn
            }
            else -> null
        }
    }

    private fun setHeaders(connection: URLConnection) {
        mReqProperty.forEach {
            connection.setRequestProperty(it.key, it.value)
        }
    }
}