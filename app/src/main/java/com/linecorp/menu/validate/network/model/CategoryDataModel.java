package com.linecorp.menu.validate.network.model;

import java.util.ArrayList;

public class CategoryDataModel {



    private String JSON_PARENT_ID = "parentId";
    private String JSON_IDS = "ids";

    public int parentId = 0;
    public ArrayList<Integer> ids= new ArrayList<Integer>();


    public CategoryDataModel() {
        // empty
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ " + JSON_PARENT_ID + ": ");
        sb.append(parentId);
        sb.append(", " + JSON_IDS + ": ");
        sb.append(ids);

        sb.append(" }");
        return sb.toString();
    }
}



