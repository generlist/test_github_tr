package com.linecorp.menu.validate.network.model.parsing

import com.linecorp.menu.validate.network.model.common.JsonModel


/**
 * @author hyunbung.shin@navercorp.com
 */

interface LVApiResponseModelListener<BodyModelType : JsonModel> {
    fun onLoadModel(result: LVModelResult, model: LVApiResponseModel<BodyModelType>?)
}
