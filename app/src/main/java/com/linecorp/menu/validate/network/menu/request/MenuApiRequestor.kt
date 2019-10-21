package com.linecorp.menu.validate.network.menu.request

import android.text.TextUtils
import com.linecorp.menu.validate.network.menu.api.MenuApi
import com.linecorp.menu.validate.network.model.MenuModel
import com.linecorp.menu.validate.network.model.parsing.LVApiResponseModelListener
import com.linecorp.menu.validate.network.model.parsing.LVModelResult
import com.linecorp.menu.validate.network.model.parsing.ParseUtil
import com.linecorp.menu.validate.network.util.LVModelConverter
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class MenuApiRequestor {
    INSTANCE;

    internal fun requestMenuInfo(url:String,listener: LVApiResponseModelListener<MenuModel>?): Any? {


        val call = MenuApi.createApi("http://appresources.beta.line.naver.jp/").requestMenuApi(url)

        call.enqueue(object : Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {

                try {

                        if (TextUtils.isEmpty(response.body())) {
                            listener?.onLoadModel(LVModelResult.E_API_EMPTY_RESPONSE, null)
                            return
                        }

                    val responses = response.body().toString()
                   val newResponse = JSONObject(responses)
                     val r = JSONObject()
                         r.put("body",newResponse)

                         //JSON 응답을 파싱한다.
                        val model = LVModelConverter.INSTANCE.buildMenuModel(r.toString())

                        // 생성된 모델 상태에 따라 결과를 전달한다.
                        val result = ParseUtil.checkApiResponseModelResult("",model, false)

                        if (result.isSucceeded) {
                            if (model != null) {
                                listener?.onLoadModel(result, model)
                            } else {
                                listener?.onLoadModel(result, null)
                            }
                        } else {

                            listener?.onLoadModel(LVModelResult.E_FAIL, model)
                        }

                }catch (e:Exception){

                    listener?.onLoadModel(LVModelResult.E_FAIL.apply { description = e.message?:description }, null)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {

                try {

                    listener?.onLoadModel(LVModelResult.E_API_NETWORK_ERROR, null)
                } catch (e: Exception) {
                }

            }
        })

        return call
    }

}