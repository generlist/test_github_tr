package com.linecorp.menu.validate.network.model.parsing

/**
 * @author  hyunbung.shin@navercorp.com
 * Model Parsing 후 Error code
 */

enum class LVModelResult  constructor(//api 요청시 parsing null 이고 html tag 가 포함된경우

    val errorCode: Int, var description: String
) {

    // SUCCESS
    S_OK(0, "Success"),
    S_CACHED_MODEL(1, "Cached Model"),

    // FAIL
    E_FAIL(-1, "Fail"),
    E_INVALID_PARAMETER(-2, "Invalid Parameter"),
    E_API_NETWORK_ERROR(-3, "API Network Error"),
    E_API_EMPTY_RESPONSE(-4, "API Empty Response"),
    E_API_INVALID_RESPONSE(-5, "API Invalid Response"),
    E_API_GATEWAY_ERROR(-6, "API Gateway Error"),
    E_API_RETURN_ERROR(-7, "API Return Error"),
    E_JSON_PARSE_EXCEPTION(-8, "Json Parse Exception"),
    E_XML_PARSE_EXCEPTION(-9, "XML Parse Exception"),
    E_HTML_PARSE_EXCEPTION(-10, "HTML"),
    E_LINE_CHANNEL_ERROR(-11, "Line Channel Error");

    val isSucceeded: Boolean
        get() = errorCode >= 0

    val isFailed: Boolean
        get() = errorCode < 0


    override fun toString(): String {
        return "errorCode:$errorCode"
    }

}

