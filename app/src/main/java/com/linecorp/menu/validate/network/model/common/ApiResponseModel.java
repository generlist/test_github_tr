package com.linecorp.menu.validate.network.model.common;


import android.text.TextUtils;

/**
 * API 응답 정보를 담는 모델 클래스의 기반 클래스
 * 
 * @author changhyeon.park@navercorp.com
 */
public abstract class ApiResponseModel extends JsonModel {
	// 참고: http://devcafe.nhncorp.com/APIgateway/forum/2030295
	public enum ApiGatewayErrorCode {
		// API Gateway Error Code
		APIGW_ACCESS_DENIED("023"),						// 일시적으로 접근이 제한되어 있는 서비스입니다.
		APIGW_AUTHENTICATION_FAILED("024"),				// 인증 실패하였습니다.
		APIGW_EXCEED_TIME_LIMIT("025"),					// HMAC 유효 시간 초과입니다.
		APIGW_MALFORMED_MSGPAD("026"),					// HMAC msgpad 항목이 잘못되었습니다.
		APIGW_MALFORMED_MESSAGE_DIGEST("027"),			// HMAC md 항목이 잘못되었습니다.
		APIGW_AUTHENTICATION_HEADER_NOT_EXISTS("028"),	// OAuth Header가 없습니다.
		APIGW_OAUTH_AUTHENTICATION_FAILED("029"),		// 요청한 Authorization 값을 확인할 수 없습니다.
		APIGW_HTTPS_PROTOCOL_IS_REQUIRED("030"),		// OAuth2 인증은 https 프로토콜을 사용해야 합니다.
		APIGW_API_NOT_EXISTS("051"),					// 존재하지 않는 API입니다.
		APIGW_MALFORMED_URL("061"),						// 잘못된 형식의 호출 URL입니다.
		APIGW_MALFORMED_PARAMETER("062"),				// 잘못된 형식의 호출 parameter입니다.
		APIGW_MALFORMED_ENCODING("063"),				// 잘못된 형식의 인코딩 문자입니다.
		APIGW_MALFORMED_HTTP_METHOD("064"),				// 지원하지 않는 HTTP METHOD입니다.
		APIGW_UNSUPPORTED_RETURN_FORMAT("071"),			// 지원하지 않는 리턴 포맷입니다.
		APIGW_AUTHENTICATION_INVALID_RESPONSE_FAILED("082"),			// 인증 서버로부터 200 OK 응답이 아닌 상태 전달받은 경우 (500 Server error 포함) 인증 세분화
		APIGW_AUTHENTICATION_CONNECTION_ERROR_FAILED("083"),			// 인증 서버의 Connection 상태 이상 (connection/read timeout 포함)
		APIGW_INTERNAL_SERVER_ERROR("080"),

		
		// Open API Error Code
		OPENAPI_SYSTEM_ERROR("000"),								// 시스템 에러
		OPENAPI_YOUR_QUERY_REQUEST_COUNT_IS_OVER_THE_LIMIT("010"),	// 쿼리한도가 초과되었습니다.
		OPENAPI_INCORRECT_QUERY_REQUEST("011"),						// 잘못된 쿼리요청입니다.
		OPENAPI_UNREGISTERED_KEY("020"),							// 등록되지 않은 키입니다.
		OPENAPI_YOUR_KEY_IS_TEMPORARY_UNAVAILABLE("021"),			// 사용할 수 없는 키입니다.
		OPENAPI_INVALID_TARGET_VALUE("100"),						// 부적절한 target 값입니다.
		OPENAPI_INVALID_DISPLAY_VALUE("101"),						// 부적절한 display 값입니다.
		OPENAPI_INVALID_START_VALUE("102"),							// 부적절한 start 값입니다.
		OPENAPI_UNDEFINED_SORT_VALUE("110"),						// 정의되지 않은 sort 값입니다.
		OPENAPI_YOU_DONT_HAVE_PERMISSION_TO_USE_THIS_FIELD("200"),	// 사용할 수 없는 field 값입니다.
		OPENAPI_AUTHENTICATION_FAILED("540"),						// OAuth Header가 없습니다.
		OPENAPI_OAUTH_AUTHENTICATION_FAILED("541"),					// 요청한 Authorization 값을 확인할 수 없습니다.
		OPENAPI_OAUTH_AUTHENTICATOIN_INVALID_USER_INFO("542"),		// 요청한 사용자 정보를 확인할 수 없습니다.
		OPENAPI_INVALID_MAP_KEY("550"),								// 지도키 파라미터가 존재하지 않습니다.
		OPENAPI_MAP_KEY_AUTHENTICATION_FAILED("551"),				// 지도키 인증에 실패하였습니다.
		OPENAPI_UNDEFINED_ERROR_OCCURED("900"),						// 정의되지 않은 오류가 발생하였습니다.
		
		// App Auth Error Code
		APPAUTH_FAILED_TO_DECODE("301"), 
		APPAUTH_EXPIRED_BASE_ACCESS_TOKEN("302"),
		APPAUTH_EXPIRED_ACCESS_TOKEN("303"),
		APPAUTH_INVALID_ACCESS_TOKEN("304"),
		APPAUTH_INVALID_NONCE("305"),
		APPAUTH_NOT_FOUND_BASE_ACCESS_TOKEN("306"),
		APPAUTH_NOT_FOUND_AID_BASE_ACCESS_TOKEN("307"),
		APPAUTH_UNREGISTERED_AID("308"),
		APPAUTH_NOT_FOUND_API("309"),
		APPAUTH_INVALID_ACCESS_TOKEN_FORMAT("310"),
		APPAUTH_UNKNOWN("311"),
		
		UNDEFINED_CODE("");
		
		public String value;
		
		private ApiGatewayErrorCode(String errorCode) {
			value = errorCode;
		}
		
		public static ApiGatewayErrorCode fromString(String str) {
			if (!TextUtils.isEmpty(str)) {
				for (ApiGatewayErrorCode errorCode : ApiGatewayErrorCode.values()) {
					if (errorCode.value.equals(str)) {
						return errorCode;
					}
				}
				
				ApiGatewayErrorCode errorCode = UNDEFINED_CODE;
				errorCode.value = str;
				
				return errorCode;
			}
			
			return UNDEFINED_CODE;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	protected static final String JSON_APIGW_ERROR_CODE = "errorMessage";
	protected static final String JSON_APIGW_MESSAGE = "errorCode";
	
	public ApiGatewayErrorCode apigwErrorCode = null;	// API GW 오류 코드
	public String apigwMessage = null;					// API GW 메시지
	
	public ApiResponseModel() {
		// empty
	}
	
	public boolean isApiGatewayError() {
		return (apigwErrorCode != null);
	}
	
	public abstract boolean isValidFormat();
	public abstract boolean isValidContent();
	public abstract boolean isError();
	public abstract String getCode();
	public abstract String getSubCode();
	public abstract String getMessage();
	public abstract String getBodyClassName();
}
