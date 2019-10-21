package com.linecorp.menu.validate.network.model.common;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * JSON 데이터 모델을 목록 형태로 담는 클래스
 * 
 * @author changhyeon.park@navercorp.com
 * @param <ModelType> JsonModel을 상속하는 모델 클래스
 */
public class JsonModelList<ModelType extends JsonModel> extends ArrayList<ModelType> {
	private static final String TAG = "MODEL_JsonModelList";
	private static final long serialVersionUID = 1L;
	
	public JsonModelList() {
		// empty
	}

	public JsonModelList(@NonNull Collection<? extends ModelType> collection) {
		super(collection);
	}

	/**
	 * JSON Object Array를 파싱해서 목록을 생성한다.
	 */
	public JsonModelList(JsonParser parser, Class<ModelType> modelClass) throws IOException {
		if (parser != null) {
			JsonToken token = parser.nextToken();
			while (parser.nextToken() != JsonToken.END_ARRAY) {
				if (token == JsonToken.START_OBJECT) {
					try {
						ModelType model = modelClass.newInstance();
						model.loadJson(parser);

						add(model);
					} catch (InstantiationException e) {
					} catch (IllegalAccessException e) {
					}
				}
			}
		}
	}


	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (ModelType model : this) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(model);
		}

		return "[ " + sb.toString() + " ]";
	}
}
