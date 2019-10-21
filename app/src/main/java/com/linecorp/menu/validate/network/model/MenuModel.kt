package com.linecorp.menu.validate.network.model

import android.text.TextUtils
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.linecorp.menu.validate.network.model.common.JsonModel
import com.linecorp.menu.validate.network.model.common.JsonModelList
import java.io.IOException

class MenuModel : JsonModel {

    companion object {
        private const val TAG = "MenuModel"

        private val JSON_CATEGORY_LIST = "categoryList"
        private val JSON_MORE_TAB_LIST = "moreTabList"
        private val JSON_SUB_MORE_TAB_LIST = "subMoreTabList"
        private val JSON_ITEM_AREA = "itemArea"
        private val JSON_DATA = "data"

    }

     var categoryList:CategoryListModel? = null
     var moreTabList :MoreTabModel? = null
     var subMoreTabList :SUBMoreTabModel? = null
     var itemArea :ItemAreaModel? = null
    var  data :JsonModelList<DataModel>? = null

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

                val token = parser.nextToken()
                if (JSON_CATEGORY_LIST == fieldName) {
                    if (token == JsonToken.START_OBJECT) {
                        categoryList = CategoryListModel(parser)
                        continue
                    }
                }
               else  if (JSON_MORE_TAB_LIST == fieldName) {
                    if (token == JsonToken.START_OBJECT) {
                        moreTabList = MoreTabModel(parser)
                        continue
                    }
                }
                else if (JSON_SUB_MORE_TAB_LIST == fieldName) {
                    if (token == JsonToken.START_OBJECT) {
                        subMoreTabList=SUBMoreTabModel(parser)
                        continue
                    }
                } else if (JSON_ITEM_AREA == fieldName) {
                        if (token == JsonToken.START_OBJECT) {
                            itemArea = ItemAreaModel(parser)
                            continue
                        }
                    } else if (JSON_DATA == fieldName) {
                    if (token == JsonToken.START_ARRAY) {
                        data = JsonModelList<DataModel>(parser,DataModel::class.java)
                        continue
                    }
                }
                ignoreUnknownField(parser, token)
            }
        }

    }

    override  fun toString(): String {

        val sb = StringBuilder()
        sb.append("{ $JSON_CATEGORY_LIST: ")
        sb.append(categoryList)
        sb.append("},")
        sb.append("{ $JSON_MORE_TAB_LIST: ")
        sb.append(moreTabList)
        sb.append("},")
        sb.append("{ $JSON_SUB_MORE_TAB_LIST: ")
        sb.append(subMoreTabList)
        sb.append("},")
        sb.append("{ $JSON_ITEM_AREA: ")
        sb.append(itemArea)
        sb.append("},")
        sb.append("{ $JSON_DATA: ")
        sb.append(data)
        sb.append("}")

        return sb.toString()
    }

}