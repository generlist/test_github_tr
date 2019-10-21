package com.linecorp.menu.validate.network.model.common;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

/**
 * API 응답 헤더 정보를 담는 모델 클래스
 *
 * @author hyunbung.shin@navercorp.com
 */
public class HeaderModel extends JsonModel {
    public enum OpenApiErrorCode {
        SUCCESS(0),
        UNKNOWN_EXCEPTION(-1),
        //Open Api Error
        LineTvSDKEncryptParamException(-100),
        LineTvSDKDecryptException(-101),
        LineTvPlayListNotExistException(-102),
        LineTvApiCompatibilityException(-103), //하위 버전을 요청할 경우
        LineTvRmcInfraException(-104),
        LineTvCountryCodeNotFoundException(-105),
        LineTvClipCountryLimitException(-106),
        LineTvClipNotExistException(-107),
        LineTvClipNotExposureException(-108),
        LiveNotFoundException(-109),
        LiveNotExposureException(-110),
        LineTvSerivceTypeInValidException(-111),
        LineTvExternalPlayNotAllowException(-112),
        //OPEN API 에러
        UNDEFINED_CODE(Integer.MAX_VALUE);

        public int value;

        private OpenApiErrorCode(int code) {
            value = code;
        }

        public static OpenApiErrorCode fromNumber(int num) {
            for (OpenApiErrorCode code : OpenApiErrorCode.values()) {
                if (code.value == num) {
                    return code;
                }
            }

            OpenApiErrorCode code = UNDEFINED_CODE;
            code.value = num;

            return code;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }

    private static final String JSON_CODE = "code";
    private static final String JSON_MESSAGE = "message";

    public OpenApiErrorCode code;	// 결과 코드
    public String message;	// 결과 메시지

    public HeaderModel() {
        // empty
    }

    public HeaderModel(OpenApiErrorCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public HeaderModel(JsonParser parser) throws IOException {
        loadJson(parser);
    }

    @Override
    public void loadJson(JsonParser parser) throws IOException {
        if (parser != null) {
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = parser.getCurrentName();
                if (TextUtils.isEmpty(fieldName)) {
                    continue;
                }

                JsonToken token = parser.nextToken();
                if (JSON_CODE.equals(fieldName)) {
                    if (token == JsonToken.VALUE_NUMBER_INT) {
                        code = OpenApiErrorCode.fromNumber(parser.getIntValue());
                        continue;
                    }
                } else if (JSON_MESSAGE.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        message = parser.getText();
                        continue;
                    }
                }

                ignoreUnknownField(parser, token);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{ " + JSON_CODE + ": ");
        sb.append(code == null ? null : code.value);
        sb.append(", " + JSON_MESSAGE + ": ");
        sb.append(message);
        sb.append(" }");

        return sb.toString();
    }
}
