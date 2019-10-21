package com.linecorp.menu.validate.network.exception

/**
 * @author hyunbung.shin@navercorp.com
 */

internal class LVNetworkError : Exception {


    companion object {
        private const val TAG ="LVNetworkError"
        private val serialVersionUID = -6469766654369165864L
    }

    var httpStatusCode: Int = 0
    var t: Throwable? =null

    constructor(httpStatusCode: Int) : super() {
        this.httpStatusCode = httpStatusCode
    }

    constructor(httpStatusCode: Int, t: Throwable?) : super() {
        this.httpStatusCode = httpStatusCode
        this.t = t
    }

    constructor(cause: Throwable) : super(cause) {}


}
