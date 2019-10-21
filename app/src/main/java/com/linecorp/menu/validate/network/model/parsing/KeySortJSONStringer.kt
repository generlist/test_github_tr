package com.linecorp.menu.validate.network.model.parsing

import org.json.JSONException
import org.json.JSONObject
import org.json.JSONStringer
import java.util.*


internal class KeySortJSONStringer : JSONStringer() {
    @Throws(JSONException::class)
    override fun value(value: Any?): JSONStringer {
        if (value != null && value is JSONObject) {
            val json = value as JSONObject?

            toSortedString(this, json!!)

            return this
        }

        return super.value(value)
    }

    companion object {

        @Throws(JSONException::class)
        fun toSortedString(json: JSONObject): String {
            val stringer = KeySortJSONStringer()
            toSortedString(stringer, json)
            return stringer.toString()
        }

        @Throws(JSONException::class)
        private fun toSortedString(stringer: JSONStringer, json: JSONObject) {
            val keys = ArrayList<String>()
            val keyIterator = json.keys()
            while (keyIterator.hasNext() == true) {
                keys.add(keyIterator.next())
            }

            Collections.sort(keys)

            stringer.`object`()
            for (key in keys) {
                stringer.key(key).value(json.opt(key))
            }
            stringer.endObject()
        }
    }

}