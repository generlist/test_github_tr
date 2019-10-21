package com.linecorp.menu.validate.network.interceptor

import android.text.TextUtils
import com.linecorp.menu.validate.network.config.LVNetworkPolicy
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.RealResponseBody
import okio.GzipSource
import okio.Okio
import java.io.IOException

/**
 * OPEN API 및 네트워크 통신을 할때 공통적으로 적용이 되야하는 것에 대한 정의
 * 1.header setting
 * @author sinhyeonbong on 2017. 2. 8..
 */

 internal class LVNetworkInterceptor() : Interceptor {

    private  var TAG = "LineTVNetworkInterceptor"

    companion object {
        private const val ACCEPT_ENCODING = "gzip"
        private const val CONTENT_ENCODING = "Content-Encoding"
        private const val CONTENT_LENGTH = "Content-Length"
    }

    constructor(tag: String) : this() {
        this.TAG = tag
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val newBuilder = originalRequest.newBuilder()
        val t1 = System.nanoTime()
        try {
        
            newBuilder.header("Accept-Encoding", ACCEPT_ENCODING)
            newBuilder.header("User-Agent", LVNetworkPolicy().getUserAgent())
            newBuilder.method(originalRequest.method(), originalRequest.body())

        } catch (e: Exception) {
        }

        //여기서 Exception 이면 다시 고민
        val response = chain.proceed(newBuilder.build())
        val t2 = System.nanoTime()

        var isGzipRequest = false
        if (response.header(CONTENT_ENCODING) != null) {
            val encoding = response.header(CONTENT_ENCODING)

            if (TextUtils.isEmpty(encoding) == false && encoding!!.contains("gzip")) {
                isGzipRequest = true
            }
        }

        val newResponse = if (isGzipRequest) {
            parsingResponse(response)
        } else response

        return newResponse

    }


    @Throws(IOException::class)
    private fun parsingResponse(response: Response): Response {

        try {
            if (response.body() == null) {
                return response
            }

            val responseBody = GzipSource(response.body()!!.source())
            val strippedHeaders = response.headers().newBuilder()
                .removeAll(CONTENT_ENCODING)
                .removeAll(CONTENT_LENGTH)
                .build()
            return response.newBuilder()
                .headers(strippedHeaders)
                .body(
                    RealResponseBody(
                        response.body()!!.contentType()!!.toString(),
                        response.body()!!.contentLength(),
                        Okio.buffer(responseBody)
                    )
                )
                .build()
        } catch (e: Exception) {
            return response
        }

    }

}
