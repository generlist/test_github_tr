package com.linecorp.menu.validate.network.util


import android.net.Uri
import android.text.TextUtils
import com.linecorp.menu.validate.network.model.parsing.KeySortJSONStringer
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*

/**
 *
 * @autor hyunbung.shin@navercorp.com
 */

object UrlMaker {

    private const val TAG = "UrlMaker"
    val LINETV_DOMAIN_DEV_NAME="tv-qa.line.me"
    val LINETV_DOMAIN_REAL_NAME="tv.line.me"

    fun urlEncode(text: String): String? {
        if (TextUtils.isEmpty(text)) {
            return text
        }
        var urlEncodedText: String?

        try {
            urlEncodedText = URLEncoder.encode(text, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            urlEncodedText = text
        }

        return urlEncodedText
    }

    fun putParam(jsonObject: JSONObject, name: String, param: Int) {
        try {
            jsonObject.put(name, param)
        } catch (ignore: JSONException) {
            // do nothing
        }
    }

    @Throws(JSONException::class)
    fun putParam(jsonObject: JSONObject, name: String, param: Double) {
        jsonObject.put(name, param)
    }

    fun putParam(jsonObject: JSONObject, name: String, param: Boolean) {
        try {
            jsonObject.put(name, param)
        } catch (ignore: JSONException) {
            // do nothing
        }

    }

    fun putParam(jsonObject: JSONObject, name: String, param: Long) {
        try {
            jsonObject.put(name, param)
        } catch (ignore: JSONException) {
            // do nothing
        }

    }

    fun putParam(jsonObject: JSONObject, name: String, param: String?) {
        try {
            jsonObject.put(name, param ?: "")
        } catch (ignore: JSONException) {
            // do nothing
        }

    }

    fun putParam(jsonObject: JSONObject, name: String, param: JSONObject?) {
        try {
            jsonObject.put(name, param ?: JSONObject())
        } catch (ignore: JSONException) {
            // do nothing
        }

    }

    @Throws(JSONException::class)
    fun putParam(jsonObject: JSONObject, name: String, param: Any?) {
        jsonObject.put(name, param ?: "")
    }

    fun getSortedKeyJson(json: JSONObject): String {
        try {
            return KeySortJSONStringer.toSortedString(json)
        } catch (e: Exception) {
        } catch (e: Throwable) {
        }

        //무언가 오류가 발생시에는 기존꺼로 그냥 사용
        return json.toString()
    }


    fun getSortedHashMapJson(bodyFields: HashMap<String, String>): Map<String, String> {
        try {
            return TreeMap<String, String>(bodyFields)

        } catch (e: Exception) {
        } catch (e: Throwable) {
        }

        //무언가 오류가 발생시에는 기존꺼로 그냥 사용
        return bodyFields
    }



    enum class VideoPath  constructor(
        val pathPrefix: String,
        val valuablePathIndex: Int
    ) {
        VOD("/v/", 1),
        LIVE("/special/live/", 2),
        PLAYLIST("/list/",3)
    }

     fun splitFirstPart(source: String): String? {
        return if (TextUtils.isEmpty(source) == true || source.indexOf("_") < 0) {
            source
        } else source.substring(0, source.indexOf("_"))

    }

    fun isNumber(str: String): Boolean {
        return str.matches("^-?[0-9]+(\\.[0-9]+)?$".toRegex())
    }

    fun getVideoRecognition(serverType: String, patternUrl: String?): Pair<Int,Int> {

        if (patternUrl == null) {
        }
        val uri = Uri.parse(patternUrl)
        val host = uri.host
        val path = uri.path
        val paths = uri.pathSegments
        val serverDomainName = if(serverType == "DEV") LINETV_DOMAIN_DEV_NAME else LINETV_DOMAIN_REAL_NAME

        if (host == serverDomainName) {
            if (path?.startsWith(VideoPath.VOD.pathPrefix) == true) {

                patternUrl?.let {

                    val index = path.indexOf(VideoPath.VOD.pathPrefix)
                    val lastPath = it.substring(index, it.length)
                    var clipNo  = 0
                    var playListNo = 0
                    val clipValuablePart =
                        splitFirstPart(paths.get(VideoPath.VOD.valuablePathIndex))

                    if (isNumber(clipValuablePart!!)) {
                        clipNo = Integer.parseInt(clipValuablePart)
                    }

                    if (path.contains(VideoPath.PLAYLIST.pathPrefix)) {

                        val playListValuablePart =
                            splitFirstPart(
                                paths.get(VideoPath.PLAYLIST.valuablePathIndex)
                            )
                        playListValuablePart?.let{
                                playListNo = Integer.parseInt(playListValuablePart)


                        }
                    }
                    return Pair<Int,Int>(clipNo,playListNo)
                }

            } else if (path?.startsWith(VideoPath.LIVE.pathPrefix) == true) {
                var liveNo = 0
                val clipValuablePart =
                    splitFirstPart(paths.get(VideoPath.LIVE.valuablePathIndex))

                clipValuablePart?.let{
                    if (isNumber(clipValuablePart)) {
                        liveNo = Integer.parseInt(clipValuablePart)
                    }
                    return Pair<Int, Int>(0, liveNo)
                }
            }else{
            }
        }
        return Pair<Int,Int>(0,0)
    }

}
