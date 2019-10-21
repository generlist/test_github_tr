package com.linecorp.menu.validate.network.model;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.linecorp.menu.validate.network.model.common.JsonModel;
import com.linecorp.menu.validate.network.model.common.JsonModelList;

import java.io.IOException;
import java.util.ArrayList;

public class MoreTabModel extends JsonModel {



    private String JSON_VERSION = "version";
    private String JSON_SPEC = "spec";
    private String JSON_IDS = "ids";

    public int version= 0;
    public String spec= null;
    public ArrayList<Integer> ids= new ArrayList<Integer>();


    public MoreTabModel() {
        // empty
    }

    public MoreTabModel(JsonParser parser) throws IOException {
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

                if (JSON_VERSION.equals(fieldName)) {
                    if (token == JsonToken.VALUE_NUMBER_INT) {
                        version = parser.getIntValue();
                        continue;
                    }

                } if (JSON_SPEC.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        spec = parser.getText();
                        continue;
                    }

                }
                else if (JSON_IDS.equals(fieldName)) {
                    if (token == JsonToken.START_ARRAY) {

                        if (parser != null) {
                            while ((token = parser.nextToken()) != JsonToken.END_ARRAY) {
                                if (token == JsonToken.VALUE_NUMBER_INT ) {
                                    try {
                                        ids.add(parser.getIntValue());
                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                }
                            }
                        }
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
        sb.append("{ " + JSON_VERSION + ": ");
        sb.append(version);
        sb.append(", " + JSON_VERSION + ": ");
        sb.append(ids);
        sb.append(", " + JSON_SPEC + ": ");
        sb.append(spec);
        sb.append(" }");
        return sb.toString();
    }
}



