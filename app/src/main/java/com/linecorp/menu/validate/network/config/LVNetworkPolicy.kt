

package com.linecorp.menu.validate.network.config

import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.webkit.WebSettings

/**
 * network api policy
 * @author hyunbung.shin@navercorp.com
 */
class LVNetworkPolicy constructor(

    currentTimeout: Long = DEFAULT_TIMEOUT_MS,
    maxNumRetries: Int = DEFAULT_API_RETRIES,
    backoffMultiplier: Float = DEFAULT_BACKOFF_MULT) {

    @JvmSynthetic
    //getset 을 만들지 않음
    @JvmField
    var maxNumRetries = 3
    var backoffMultiplier=1.0F
    var currentTimeout =5_000L



    companion object {
        private const val TAG = "LVPlayerPolicy"

        //Player
        const val PLAYER_TIMEOUT_MS = 10_000L
        @JvmSynthetic
        internal const val PLAYER_RETRIES = 3
        @JvmSynthetic
        internal const val PLAYER_BACKOFF_MULT = 1.0f
        @JvmSynthetic
        val RETROFIT_KEEP_ALIVE_DURATION = 5 //5분
        @JvmSynthetic
        val RETROFIT_MAX_IDLE_CONNECTION = 5 //5분

        var cacheTime = 120 //120초
        var cacheSize = 50 * 1024 * 1024; //50MB

        //open api
        @JvmSynthetic
        internal const val DEFAULT_TIMEOUT_MS = 5_000L
        @JvmSynthetic
        internal const val DEFAULT_API_RETRIES = 3
        @JvmSynthetic
        internal const val DEFAULT_BACKOFF_MULT = 1.0f

        //statsapi
        @JvmSynthetic
        internal const val STATS_API_TIMEOUT = 5_000L;
        @JvmSynthetic
        internal const val STATS_API_RETRY = 1;
        @JvmSynthetic
        internal const val STATS_API_BACKOFFMUL = 1.0f;

    }

    //WebView에서 사용하는 userAgent 를 저장
    private var mFullUserAgent: String? = null
    private var mPostUserAgent: String? = null


    init {
        this.maxNumRetries = maxNumRetries
        this.currentTimeout =currentTimeout
        this.backoffMultiplier=backoffMultiplier
    }



    /**
     * UserAgent String을 생성하여 Return
     * @return
     */
    private val postUserAgent: String
        get() {
            if (mPostUserAgent == null) {
                mPostUserAgent = "LINE(inapp; LINETVSDK; 100; " + "0.0.1" + ")"
            }

            return mPostUserAgent as String
        }

    /**
     * 네이버 규칙을 따르는 Full UserAgent를 반환
     * @return
     */
    @JvmSynthetic
    fun getUserAgent(): String {

        return mFullUserAgent ?: builtUserAgent
    }

    /**
     * 예외상황에 대한 userAgent
     */

    private val builtUserAgent: String
        get() {
            val sb = StringBuilder()
            sb.append("Mozilla/0.0 (Linux; Android ")
                .append(Build.VERSION.RELEASE)
                .append("; ")
                .append(Build.MODEL)
                .append(" Build/")
                .append(Build.DISPLAY)
                .append(")")

            return sb.toString()
        }

}