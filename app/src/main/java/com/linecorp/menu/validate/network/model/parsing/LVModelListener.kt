package  com.linecorp.menu.validate.network.model.parsing

import com.linecorp.menu.validate.network.model.common.BaseModel


/**
 * @author hyunbung.shin@navercorp.com
 */

interface LVModelListener<ModelType : BaseModel> {
    fun onLoadModel(result: LVModelResult, model: ModelType)
}
