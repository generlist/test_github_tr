package com.linecorp.menu.validate.network.model.parsing;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.linecorp.menu.validate.network.model.common.ApiResponseModel;
import com.linecorp.menu.validate.network.model.common.EmptyModel;
import com.linecorp.menu.validate.network.model.common.HeaderModel;
import com.linecorp.menu.validate.network.model.common.JsonModel;

import java.io.IOException;

/**
 * LINE TV API 응답 결과를 담는 클래스
 * 
 * @author changhyeon.park@navercorp.com
 * @param <BodyModelType> JsonModel을 상속하는 모델 클래스
 */
public class LVApiResponseModel<BodyModelType extends JsonModel> extends ApiResponseModel {

	private static final String TAG = "MODEL_LineTvApiResponseModel";

	private static final String JSON_HEADER = "h";
	private static final String JSON_BODY = "body";


	public HeaderModel header = null;						// 헤더
	public BodyModelType body = null;						// 바디
	private Class<BodyModelType> mBodyModelClass = null;	// 바디 타입 클래스
	
	public LVApiResponseModel(Class<BodyModelType> bodyModelClass) {
		mBodyModelClass = bodyModelClass;
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

				 if (JSON_BODY.equals(fieldName)) {
					if (token == JsonToken.START_OBJECT) {
						try {
							body = mBodyModelClass.newInstance();
							body.loadJson(parser);
						} catch (InstantiationException e) {

						} catch (IllegalAccessException e) {
						}
						continue;
					} else if (token == JsonToken.START_ARRAY) {
						try {
							body = mBodyModelClass.newInstance();

							body.loadJson(parser);
						} catch (InstantiationException e) {
						} catch (IllegalAccessException e) {
						}
						continue;
					}
				} else if (JSON_APIGW_ERROR_CODE.equals(fieldName)) {
					if (token == JsonToken.VALUE_STRING) {
						apigwErrorCode = ApiGatewayErrorCode.fromString(parser.getText());
						continue;
					}
				} else if (JSON_APIGW_MESSAGE.equals(fieldName)) {
					if (token == JsonToken.VALUE_STRING) {
						apigwMessage = parser.getText();
						continue;
					}
				}
				
				ignoreUnknownField(parser, token);
			}

		}
	}
	
	@Override
	public boolean isValidFormat() {
		// JSON이 정상적으로 로드되었다면 둘 중 하나는 유효해야 한다. 
		return (header != null || apigwErrorCode != null);
	}
	
	@Override
	public boolean isValidContent() {
		if (isValidFormat() && !isError()) {
			if (EmptyModel.class.equals(mBodyModelClass)) {
				return true;
			}
			
			// body가 명시적으로 empty인 경우가 아니라면 항상 존재해야 한다.
			return (body != null);
		}
		
		return false;
	}
	
	@Override
	public boolean isError() {
		if (header != null) {
			return (header.code != HeaderModel.OpenApiErrorCode.SUCCESS);
		}
		
		return true;
	}
	
	@Override
	public String getCode() {
		if (isApiGatewayError()) {
			return apigwErrorCode.toString();
		}
		
		if (header != null) {
			return header.code.toString();
		}
		
		return null;
	}
	
	@Override
	public String getSubCode() {
		return null;
	}
	
	@Override
	public String getMessage() {
		if (isApiGatewayError()) {
			return apigwMessage;
		}
		
		if (header != null) {
			return header.message;
		}
		
		return null;
	}
	
	@Override
	public String getBodyClassName() {
		if (mBodyModelClass != null) {
			return mBodyModelClass.getSimpleName();
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("{ " + JSON_HEADER + ": ");
		sb.append(header);
		sb.append(", " + JSON_BODY + ": ");
		sb.append(body);
		if (isApiGatewayError()) {
			sb.append(", " + JSON_APIGW_ERROR_CODE + ": ");
			sb.append(apigwErrorCode.value);
			sb.append(", " + JSON_APIGW_MESSAGE + ": ");
			sb.append(apigwMessage);
		}
		sb.append(" }");
		
		return sb.toString();
	}
}
