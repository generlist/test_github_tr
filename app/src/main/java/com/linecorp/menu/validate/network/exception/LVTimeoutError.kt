package com.linecorp.menu.validate.network.exception

import java.io.IOException


/**
 * @autor hyunbung.shin@navercorp.com
 */

internal class LVTimeoutError : IOException {

    companion object {

        private const val TAG ="LVTimeoutError"

        private val serialVersionUID = -6469766654369165864L
    }

    constructor() : super() {}

    constructor(cause: Throwable) : super(cause) {}


}
