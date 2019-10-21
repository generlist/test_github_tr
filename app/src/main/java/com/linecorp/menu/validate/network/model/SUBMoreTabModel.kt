package com.linecorp.menu.validate.network.model

import android.text.TextUtils
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.linecorp.menu.validate.network.model.common.JsonModel
import com.linecorp.menu.validate.network.model.common.JsonModelList
import java.io.IOException

class SUBMoreTabModel : JsonModel {

    companion object {
        private const val TAG = "SUBMoreTabModel"

        private val JSON_VERSION = "version"
        private val JSON_SPEC = "spec"
        private val JSON_IDS = "ids"


    }

     var version: Int = 0
     var spec: String? = null
     var ids: ArrayList<Int>? = ArrayList()


    constructor() {}


    @Throws(IOException::class)
    constructor(parser: JsonParser) {
        loadJson(parser)
    }

    @Throws(IOException::class)
    override fun loadJson(parser: JsonParser?) {

        if (parser != null) {
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                val fieldName = parser.currentName
                if (TextUtils.isEmpty(fieldName)) {
                    continue
                }


                var token = parser.nextToken()
                if (JSON_SPEC == fieldName) {
                    if (token == JsonToken.VALUE_STRING) {
                        spec = parser.text
                        continue
                    }
                } else if (JSON_VERSION == fieldName) {
                    if (token == JsonToken.VALUE_NUMBER_INT) {
                        version = parser.intValue
                        continue
                    }
                } else if (JSON_IDS == fieldName) {
                    if (token == JsonToken.START_ARRAY) {

                        if (parser != null) {

                            while (parser.nextToken() != JsonToken.END_ARRAY) {
                                if (parser.nextToken() == JsonToken.VALUE_NUMBER_INT) {
                                    try {
                                        ids?.add(parser.intValue)
                                    } catch (e: Exception) {
                                        e.printStackTrace()

                                    }

                                }
                            }
                        }
                        continue
                    }
                }

                ignoreUnknownField(parser, token)
            }
        }

    }
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append(",{ ${JSON_SPEC}: ")
        sb.append(spec)
        sb.append(", ${JSON_VERSION}: ")
        sb.append(version)
        sb.append(",{ ${JSON_IDS}:} ")
        sb.append(ids)

        sb.append(" }")

        return sb.toString()
    }
}