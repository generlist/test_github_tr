package com.linecorp.menu.validate.network.model;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.linecorp.menu.validate.network.model.common.JsonModel;
import com.linecorp.menu.validate.network.model.common.JsonModelList;

import java.io.IOException;
import java.util.ArrayList;

public class ItemAreaModel extends JsonModel {



    private String JSON_VERSION = "version";
    private String JSON_DATA = "data";
    private String JSON_POSITON = "position";
    private String JSON_IDS = "ids";

    public int version= 0;
    public String position = null;
    public ArrayList<Integer> ids= new ArrayList<Integer>();
    public ArrayList<ItemAreaDataModel> data= new ArrayList<ItemAreaDataModel>();
    public ItemAreaDataModel itemAreaDataModel = null;

    public ItemAreaModel() {
        // empty
    }

    public ItemAreaModel(JsonParser parser) throws IOException {
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

                } else if (JSON_DATA.equals(fieldName)) {

                    if (token == JsonToken.START_ARRAY) {

                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                            if (TextUtils.isEmpty(fieldName)) {
                                continue;
                            }
                            itemAreaDataModel = new ItemAreaDataModel();
                            while (parser.nextToken() != JsonToken.END_OBJECT) {
                                String fieldNames = parser.getCurrentName();

                                if (TextUtils.isEmpty(fieldNames)) {
                                    continue;
                                }
                                JsonToken tokens = parser.nextToken();

                                if (JSON_POSITON.equals(fieldNames)) {

                                    if (tokens == JsonToken.VALUE_STRING) {
                                        itemAreaDataModel.position = parser.getText();
                                        continue;
                                    }
                                }else if (JSON_IDS.equals(fieldNames)) {

                                    if (tokens == JsonToken.START_ARRAY) {
                                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                                            ids.add(parser.getIntValue());
                                        }
                                    }
                                    itemAreaDataModel.ids.addAll(ids);
                                }
                            }
                            data.add(itemAreaDataModel);

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
        sb.append(", " + JSON_DATA + ": ");
        sb.append(data);
        sb.append(" }");
        return sb.toString();
    }
}



