package com.linecorp.menu.validate.network.model;

import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.linecorp.menu.validate.network.model.common.JsonModel;
import com.linecorp.menu.validate.network.model.common.JsonModelList;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;

public class CategoryListModel extends JsonModel {



    private String JSON_VERSION = "version";
    private String JSON_SPEC = "spec";
    private String JSON_DATA = "data";
    private String JSON_PARENT_ID="parentId";
    private String JSON_IDS="ids";
     public int version= 0;
     public String spec = null;
    public ArrayList<Integer> ids= new ArrayList<Integer>();
    public ArrayList<CategoryDataModel> data= new ArrayList<CategoryDataModel>();
    public CategoryDataModel categoryDataModel = null;
    public CategoryListModel() {
        // empty
    }

    public CategoryListModel(JsonParser parser) throws IOException {
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

                if (JSON_SPEC.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        spec = parser.getText();
                        continue;
                    }

                } else if (JSON_VERSION.equals(fieldName)) {
                    if (token == JsonToken.VALUE_NUMBER_INT) {
                        version = parser.getIntValue();
                        continue;
                    }
                }
                else if (JSON_DATA.equals(fieldName)) {
                    if (token == JsonToken.START_ARRAY) {


                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                            if (TextUtils.isEmpty(fieldName)) {
                                continue;
                            }
                            categoryDataModel = new CategoryDataModel();
                            while (parser.nextToken() != JsonToken.END_OBJECT) {
                                String fieldNames = parser.getCurrentName();

                                if (TextUtils.isEmpty(fieldNames)) {
                                    continue;
                                }
                                JsonToken tokens = parser.nextToken();

                                if (JSON_PARENT_ID.equals(fieldNames)) {

                                    if (tokens == JsonToken.VALUE_NUMBER_INT) {
                                        categoryDataModel.parentId = parser.getIntValue();
                                        continue;
                                    }
                                }else if (JSON_IDS.equals(fieldNames)) {

                                    if (tokens == JsonToken.START_ARRAY) {
                                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                                            ids.add(parser.getIntValue());
                                        }
                                    }
                                    categoryDataModel.ids.addAll(ids);
                                }
                            }
                            data.add(categoryDataModel);

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
        sb.append("{ " + JSON_SPEC + ": ");
        sb.append(spec);
        sb.append(", " + JSON_VERSION + ": ");
        sb.append(version);
        sb.append(", " + JSON_DATA + ": ");
        sb.append(data);
        sb.append(" }");
        return sb.toString();
    }
}


