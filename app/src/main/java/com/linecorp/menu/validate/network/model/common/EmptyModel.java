package com.linecorp.menu.validate.network.model.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

/**
 * 빈 모델 클래스
 * 
 * @author changhyeon.park@navercorp.com
 */
public class EmptyModel extends JsonModel {
	public EmptyModel() {
		// empty
	}
	
	public EmptyModel(JsonParser parser) throws IOException {
		loadJson(parser);
	}
	
	@Override
	public void loadJson(JsonParser parser) throws IOException {
		if (parser != null) {
			while (parser.nextToken() != JsonToken.END_OBJECT) {
				JsonToken token = parser.nextToken();
				ignoreUnknownField(parser, token);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{}");
		
		return sb.toString();
	}
}
