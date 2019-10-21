package com.linecorp.menu.validate.network.interceptor


import android.text.TextUtils
import com.linecorp.menu.validate.network.config.LVNetworkPolicy
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http.RealResponseBody
import okio.GzipSource
import okio.Okio
import java.io.IOException

/**
 * 캐시를 위한 network Intercepter
 * @author shinhyunbung on 2017. 4. 22..
 */

class LVResponseCacheInterceptor(val chache: Boolean) : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        var response = chain.proceed(internalRequestUrl(originalRequest))

        try {
            val newBuilder = originalRequest.newBuilder()

            newBuilder.header("Accept-Encoding", "gzip")
            newBuilder.header("User-Agent",  LVNetworkPolicy().getUserAgent())
            newBuilder.header("Content-Type", "application/json; charset=utf-8")
            newBuilder.method(originalRequest.method(), originalRequest.body())

            //chain 응답 값에 캐시 셋팅
            val resBuilder = response.newBuilder()

            resBuilder.header("Content-Type", "application/json; charset=utf-8")
            if (chache) {
                val cacheTime =LVNetworkPolicy.cacheTime
                resBuilder.header("Cache-Control", "public, max-age=$cacheTime")
            } else {
                resBuilder.header("Cache-Control", "public, max-age=" + 0)
            }

            resBuilder.request(originalRequest)
            response = resBuilder.build()

            var isGzipRequest = false
            if (response.header("Content-Encoding") != null) {
                val encoding = response.header("Content-Encoding")

                if (TextUtils.isEmpty(encoding) == false && encoding!!.contains("gzip")) {
                    isGzipRequest = true
                }
            }


            return if (isGzipRequest) {
                parsingResponse(response)
            } else response

        } catch (e: Exception) {

        }

        //Exception 이 발생하더라도 chain process 는 한번만 진행하도록 하며 문제 발생시 요청한 ui 에서 처리함
        return response
    }

    private fun internalRequestUrl(request: Request): Request {
        try {
            var url = request.url().toString()
        } catch (e: Exception) {
            return request
        }

        return request
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

    companion object {
        private val TAG = "LVResponseCacheInterceptor"
        private const val ACCEPT_ENCODING = "gzip"
        private const val CONTENT_ENCODING = "Content-Encoding"
        private const val CONTENT_LENGTH = "Content-Length"

    }
}
