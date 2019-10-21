package com.linecorp.menu.validate.network.util;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.linecorp.menu.validate.network.model.MenuModel;
import com.linecorp.menu.validate.network.model.common.ApiResponseModel;
import com.linecorp.menu.validate.network.model.common.JsonModel;
import com.linecorp.menu.validate.network.model.parsing.LVApiResponseModel;


import java.io.IOException;


/**
 * API 응답을 가져와 Model Type 으로 반환하는 Converter 이다
 * @author sinhyeonbong on 2017. 2. 10..
 */

public enum LVModelConverter {

    INSTANCE; // Singleton

    private static final String TAG = "LVModelConverter";

    private JsonFactory mFactory = new JsonFactory();



    public LVApiResponseModel<MenuModel> buildMenuModel(String json) {
        return buildLineTvApiResponseModel(json, MenuModel.class);
    }





    /**
     * LINE TV API 응답 결과 모델을 생성한다.
     * @param json 응답 JSON 문자열
     * @param bodyModelClass body 필드를 구성하는 모델 클래스
     * @return 생성 모델
     */
    private <BodyModelType extends JsonModel> LVApiResponseModel<BodyModelType> buildLineTvApiResponseModel(String json, Class<BodyModelType> bodyModelClass) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        return loadApiResponseJson(json, new LVApiResponseModel<BodyModelType>(bodyModelClass));
    }


    /**
     * JSON 데이터를 파싱해서 API 응답 결과 모델에 로드한다.
     * @param json 응답 JSON 문자열
     * @param apiResponseModel API 응답 결과 모델 인스턴스
     * @return 생성 모델
     */
    private <ApiResponseModelType extends ApiResponseModel> ApiResponseModelType loadApiResponseJson(String json, ApiResponseModelType apiResponseModel) {
        try {

            JsonParser parser = mFactory.createParser(json);

            if (parser.nextToken() == JsonToken.START_OBJECT) {
                apiResponseModel.loadJson(parser);
            }
            parser.close();

            return apiResponseModel;
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();

            sb.append(apiResponseModel.getClass().getSimpleName());
            String bodyClassName = apiResponseModel.getBodyClassName();
            if (!TextUtils.isEmpty(bodyClassName)) {
                sb.append('<');
                sb.append(bodyClassName);
                sb.append('>');
            }

        }

        return null;
    }


    public JsonFactory getFactory() {
        return mFactory;
    }
}

