package com.superkind.httpapi

import com.superkind.httpapi.interfaces.HttpCallBack
import org.junit.Before
import org.junit.Test
import java.nio.charset.Charset

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HttpAPIUnitTest {
    private lateinit var api: HttpAPI
    @Before
    fun init() {
        api = HttpAPI()
    }


    /**
     * http 방식이든 https 방식이든, 모두 호출될 수 있도록 작성한다.
     */
    @Test
    fun httpUrl() {
        val url =  "http://example.org/"
        api.get(url, callback())
    }

    /**
     * http 방식이든 https 방식이든, 모두 호출될 수 있도록 작성한다.
     */
    @Test
    fun httpsUrl() {
        val url =  "https://example.org/"
        api.get(url, callback())
    }

    @Test
    fun validUrl() {
        val url =  "https://en.wikipedia.org/api/rest_v1/page/summary/google"
        api.get(url, callback())
    }

    @Test
    fun invalidUrl() {
        val url =  "https://en.wikipedia.org/api/rest_v10/page/summary/google"
        api.get(url, callback())
    }

    @Test
    fun notSupportUrl() {
        val url =  "ftp://en.wikipedia.org/api/rest_v10/page/summary/google"
        api.get(url, callback())
    }

    @Test
    fun notFound() {
        val url =  "https://en.wikipedia.org/api/rest_v10/page/summary2/google"
        api.get(url, callback())
    }

    /**
     * 요청 메서드 타입은 GET, POST, PUT, DELETE를 지원하도록 한다.
     */
    @Test
    fun testPost() {
        val url = "https://webhook.site/d7c787b0-58a2-4858-8934-a82a7e220b24"
        api.post(url, "superkind".toByteArray(Charset.defaultCharset()), callback())
    }

    /**
     * 요청 메서드 타입은 GET, POST, PUT, DELETE를 지원하도록 한다.
     */
    @Test
    fun testPut() {
        val url = "https://webhook.site/d7c787b0-58a2-4858-8934-a82a7e220b24"
        api.put(url, "superkind".toByteArray(Charset.defaultCharset()), callback())
    }

    /**
     * 요청 메서드 타입은 GET, POST, PUT, DELETE를 지원하도록 한다.
     */
    @Test
    fun testDelete() {
        val url = "https://webhook.site/d7c787b0-58a2-4858-8934-a82a7e220b24"
        api.delete(url, callback())
    }

    /**
     * Request Header 값을 설정할 수 있어야 한다.
     */
    @Test
    fun setHeader() {
        val url = "https://webhook.site/d7c787b0-58a2-4858-8934-a82a7e220b24"
        api.setRequestHeader("Content-Type", "text/html; charset=utf-8")
        api.setRequestHeader("Content-Language", "ko")
        api.put(url, "superkind".toByteArray(Charset.defaultCharset()), callback())
    }

    /**
     * Response Data를 어떤 형식의 타입으로도 요청할 수 있어야 한다.
     */
    @Test
    fun getData() {
        val url =  "https://en.wikipedia.org/api/rest_v1/page/related/google"
        api.get(url, object : HttpCallBack {
            override fun onSuccess(response: ByteArray) {
                println(String(response))
            }

            override fun onFailure(code: Int, msg: String) {
                println("code: $code msg: $msg")
            }
        })
    }

    /**
     * Request Body 전달 시 어떤 형식으로든 서버에 전달, 요청할 수 있어야 한다.(Any -> ByteArray)
     */
    @Test
    fun sendData() {
        val url = "https://webhook.site/d7c787b0-58a2-4858-8934-a82a7e220b24"
        // String -> ByteArray
        api.post(url, "NHN SOFT".toByteArray(Charset.defaultCharset()), callback())

        // Int -> ByteArray
        api.post(url, 123.toString().toByteArray(), callback())
    }

    /**
     * API 접속 timeout 시간을 설정할 수 있도록 작성한다.
     */
    @Test
    fun setTimeout() {
        val url =  "https://en.wikipedia.org/api/rest_v1/page/summary/google"
        api.setTimeout(10000) // 타임아웃 10초로 설정
        api.get(url, callback())
    }

    private fun callback(): HttpCallBack {
        return object : HttpCallBack {
            override fun onSuccess(response: ByteArray) {
                println(String(response))
            }

            override fun onFailure(code: Int, msg: String) {
                println("code: $code msg: $msg")
            }
        }
    }
}