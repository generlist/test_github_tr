package com.linecorp.menu.validate.network.model.parsing

import com.linecorp.menu.validate.network.model.common.JsonModel


/**
 * @author yealim.han@navercorp.com
 */

interface LVAuthApiResponseModelListener<BodyModelType : JsonModel> {
    fun onLoadModel(result: LVModelResult, model: BodyModelType?)
}