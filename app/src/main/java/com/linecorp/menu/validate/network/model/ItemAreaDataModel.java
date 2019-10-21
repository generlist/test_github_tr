package com.linecorp.menu.validate.network.model;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.linecorp.menu.validate.network.model.common.JsonModel;
import com.linecorp.menu.validate.network.model.common.JsonModelList;

import java.io.IOException;
import java.util.ArrayList;

public class ItemAreaDataModel {



    private String JSON_POSITON = "position";
    private String JSON_IDS = "ids";

    public String position = null;
    public ArrayList<Integer> ids= new ArrayList<Integer>();


    public ItemAreaDataModel() {
        // empty
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ " + JSON_POSITON + ": ");
        sb.append(position);
        sb.append(", " + JSON_IDS + ": ");
        sb.append(ids);

        sb.append(" }");
        return sb.toString();
    }
}



