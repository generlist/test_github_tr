package com.linecorp.menu.validate.network.model.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JSON 데이터를 파싱해서 저장하는 모델 클래스의 기반 클래스
 * 
 * @author changhyeon.park@navercorp.com
 */
public abstract class JsonModel extends BaseModel {
	public JsonModel() {
		// empty
	}
	
	public abstract void loadJson(JsonParser parser) throws IOException;
	
	// 문자열 배열로부터 ArrayList를 만든다.
	public static ArrayList<String> createStringArrayList(String[] array) {
		ArrayList<String> arrayList = new ArrayList<String>();
		
		for (String str : array) {
			arrayList.add(str);
		}
		
		return arrayList;
	}
	
	// 정수 배열로부터 ArrayList를 만든다.
	public static ArrayList<Integer> createIntegerArrayList(int[] array) {
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		
		for (int num : array) {
			arrayList.add(num);
		}
		
		return arrayList;
	}
	
	// 문자열을 담은 JSON 배열을 파싱한다.
	protected static ArrayList<String> parseStringJsonArray(JsonParser parser) throws IOException {
		if (parser != null) {
			ArrayList<String> arrayList = new ArrayList<String>();
			JsonToken token = null;
			
			while ((token = parser.nextToken()) != JsonToken.END_ARRAY) {
				if (token == JsonToken.VALUE_STRING) {
					arrayList.add(parser.getText());
				} else if (token == JsonToken.START_OBJECT) {
					ignoreUnknownObject(parser);
				} else if (token == JsonToken.START_ARRAY) {
					ignoreUnknownArray(parser);
				}
			}
			
			return arrayList;
		}
		
		return null;
	}
	
	// 정수를 담은 JSON 배열을 파싱한다.
	protected static ArrayList<Integer> parseIntegerJsonArray(JsonParser parser) throws IOException {
		if (parser != null) {
			ArrayList<Integer> arrayList = new ArrayList<Integer>();
			JsonToken token = null;
			
			while ((token = parser.nextToken()) != JsonToken.END_ARRAY) {
				if (token == JsonToken.VALUE_NUMBER_INT) {
					arrayList.add(parser.getIntValue());
				} else if (token == JsonToken.START_OBJECT) {
					ignoreUnknownObject(parser);
				} else if (token == JsonToken.START_ARRAY) {
					ignoreUnknownArray(parser);
				}
			}
			
			return arrayList;
		}
		
		return null;
	}
	
	// 모르는 이름의 JSON 필드를 만나면 무시하도록 처리한다.
	protected static void ignoreUnknownField(JsonParser parser, JsonToken token) throws IOException {
		if (token == JsonToken.START_OBJECT) {
			ignoreUnknownObject(parser);
		} else if (token == JsonToken.START_ARRAY) {
			ignoreUnknownArray(parser);
		}
	}
	
	// 모르는 이름의 JSON 오브젝트를 만났을 때 {} 짝을 맞추도록 무시해서 다른 짝들이 엉켜서 파싱하지 못하는 경우를 방지한다. 
	private static void ignoreUnknownObject(JsonParser parser) throws IOException {
		if (parser != null) {
			for (JsonToken token = parser.nextToken(); token != null && token != JsonToken.END_OBJECT; token = parser.nextToken()) {
				if (token == JsonToken.START_OBJECT) { // 현재 {}의 안쪽에 있는 {}일 경우
					ignoreUnknownObject(parser);
				} else if (token == JsonToken.START_ARRAY) { // 현재 {}의 안쪽에 있는 []일 경우
					ignoreUnknownArray(parser);
				}
			}
		}
	}
	
	// 모르는 이름의 JSON 배열을 만났을 때 [] 짝을 맞추도록 무시해서 다른 짝들이 엉켜서 파싱하지 못하는 경우를 방지한다.
	private static void ignoreUnknownArray(JsonParser parser) throws IOException {
		if (parser != null) {
			for (JsonToken token = parser.nextToken(); token != null && token != JsonToken.END_ARRAY; token = parser.nextToken()) {
				if (token == JsonToken.START_OBJECT) { // 현재 []의 안쪽에 있는 {}일 경우
					ignoreUnknownObject(parser);
				} else if (token == JsonToken.START_ARRAY) { // 현재 []의 안쪽에 있는 []일 경우
					ignoreUnknownArray(parser);
				}
			}
		}
	}
}
