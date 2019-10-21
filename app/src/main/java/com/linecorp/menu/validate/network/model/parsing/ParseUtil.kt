package com.linecorp.menu.validate.network.model.parsing

import android.text.TextUtils
import android.widget.Toast
import com.linecorp.menu.validate.network.model.common.ApiResponseModel

/**
 * Parsing validation util
 * @author hyunbung.shin@navercorp.com
 */

object ParseUtil {


    fun checkApiResponseModelResult(logTag: String, model: ApiResponseModel?, isincludeHtml: Boolean): LVModelResult {

        return LVModelResult.S_OK
    }


    private fun getApiResponseCode(model: ApiResponseModel?): String {
        if (model != null) {
            val code = model.code
            val subCode = model.subCode

            if (!TextUtils.isEmpty(code)) {
                val sb = StringBuilder()
                sb.append("(")
                sb.append(code)
                if (!TextUtils.isEmpty(subCode)) {
                    sb.append(", ")
                    sb.append(subCode)
                }
                sb.append(")")

                return sb.toString()
            }
        }

        return ""
    }

    private fun showToast(message: String) {
    }

    private fun hasApiResponseSubCodeError(model: ApiResponseModel?): Boolean {
        if (model != null) {
            val subCode = model.subCode

            if (!TextUtils.isEmpty(subCode) && "0" != subCode) {
                return true
            }
        }

        return false
    }
}
