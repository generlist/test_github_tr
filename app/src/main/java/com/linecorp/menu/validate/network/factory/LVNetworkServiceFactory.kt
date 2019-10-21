package com.linecorp.menu.validate.network.factory

import com.linecorp.menu.validate.network.config.LVNetworkPolicy
import com.linecorp.menu.validate.network.interceptor.LVNetworkInterceptor
import com.linecorp.menu.validate.network.interceptor.LVResponseCacheInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


internal class LVNetworkServiceFactory {


    companion object {

        /**
         * @param logInfo
         * @param policy
         * @param chache
         * @param appKey
         */
        @JvmSynthetic
        fun getOkHttpClient(
            policy: LVNetworkPolicy,
            cache: Boolean
        ): OkHttpClient {

            val builder = OkHttpClient.Builder()
                .readTimeout(policy.currentTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(policy.currentTimeout, TimeUnit.MILLISECONDS)
                .connectTimeout(policy.currentTimeout, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(LVResponseCacheInterceptor(cache))
                .addInterceptor(LVNetworkInterceptor("TAG"))

            return builder.build()


        }


    }
}

