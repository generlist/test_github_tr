package com.linecorp.menu.validate.network.menu.api


import com.linecorp.menu.validate.network.annotation.Retry
import com.linecorp.menu.validate.network.config.LVNetworkPolicy
import com.linecorp.menu.validate.network.factory.LVFilterCallAdapterFactory
import com.linecorp.menu.validate.network.factory.LVNetworkServiceFactory
import com.linecorp.menu.validate.network.factory.ToStringConverterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url


internal interface MenuApi{

    companion object {

        private const val TAG = "MenuApi"

        fun createApi(baseUrl: String): MenuApi {
            return Retrofit.Builder()
                    .client(
                            LVNetworkServiceFactory.getOkHttpClient(
                                    LVNetworkPolicy(
                                            LVNetworkPolicy.DEFAULT_TIMEOUT_MS,
                                            LVNetworkPolicy.DEFAULT_API_RETRIES,
                                            LVNetworkPolicy.DEFAULT_BACKOFF_MULT), cache = true))
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(
                            LVFilterCallAdapterFactory
                                    .create(LVNetworkPolicy(LVNetworkPolicy.DEFAULT_TIMEOUT_MS, LVNetworkPolicy.DEFAULT_API_RETRIES, LVNetworkPolicy.DEFAULT_BACKOFF_MULT)))
                    .addConverterFactory(ToStringConverterFactory())
                    .build()
                    .create(MenuApi::class.java)
        }
    }


    @Retry(3)
    @GET
    @Headers("Content-Type: application/json; charset=utf-8")
    fun requestMenuApi(@Url url: String): Call<String>


}