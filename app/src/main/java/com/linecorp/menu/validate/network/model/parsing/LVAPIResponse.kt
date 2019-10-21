package com.linecorp.menu.validate.network.model.parsing

import com.linecorp.menu.validate.network.model.common.JsonModel


/**
 * @author hyunbung.shin@navercorp.com
 */

internal class LVAPIResponse<BodyModelType : JsonModel> {
    val result: LVModelResult
    val model: LVApiResponseModel<BodyModelType>?

    constructor(result: LVModelResult, model: LVApiResponseModel<BodyModelType>) {
        this.result = result
        this.model = model
    }

    constructor(result: LVModelResult) {
        this.result = result
        this.model = null
    }
}
