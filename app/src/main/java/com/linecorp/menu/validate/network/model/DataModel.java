package com.linecorp.menu.validate.network.model;

import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.linecorp.menu.validate.network.model.common.JsonModel;

import java.io.IOException;

public class DataModel extends JsonModel {



    private String JSON_ID="id";
    private String JSON_VERSION         ="version";
    private String JSON_SUTYPE      ="subType";
    private String JSON_TARGET_URL  ="targetUrl";
    private String JSON_CAMPAIGNURL       ="campaignUrl";
    private String JSON_THEMEICONKEY     ="themeIconKey";
    private String JSON_NEWFLAGVERSION     ="newFlagVersion";
    private String JSON_CHANNELID      ="channelId";
    private String JSON_BADGETYPE       ="badgeType";
    private String JSON_ICONN_URL_BASE       ="iconUrlBase";
    private String JSON_TYPE      ="type";
    private String JSON_MARKET_URL       = "marketUrl";
    private String JSON_PACKAGE_NAME       ="packageName";
    private String JSON_SPEC       ="spec";
    private String JSON_UPPER_BOUNDSPEC    ="upperBoundSpec";
    private String JSON_TITLE       ="title";


    public int id = 0;
    public int version = 0;
    public int subType = 0;
    public String targetUrl = null;
    public String campaignUrl = null;
    public String themeIconKey = null;
    public int newFlagVersion = 0;
    public int channelId = 0;
    public String badgeType = null;
    public String iconUrlBase = null;
    public int type = 0;
    public String marketUrl = null;
    public String packageName = null;
    public String spec = null;
    public String upperBoundSpec = null;
    public String title;



    public DataModel() {
        // empty
    }

    public DataModel(JsonParser parser) throws IOException {
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

                if (JSON_TYPE.equals(fieldName)) {
                    if (token == JsonToken.VALUE_NUMBER_INT) {
                        type =parser.getIntValue();
                        continue;
                    }
                } else if (JSON_ID.equals(fieldName)) {
                    if (token == JsonToken.VALUE_NUMBER_INT) {
                        id = parser.getIntValue();
                        continue;
                    }
                }
                else if (JSON_VERSION.equals(fieldName)) {
                    if (token == JsonToken.VALUE_NUMBER_INT) {
                        version = parser.getIntValue();
                        continue;
                    }
                }
                else if (JSON_SUTYPE.equals(fieldName)) {
                    if (token == JsonToken.VALUE_NUMBER_INT) {
                        subType =parser.getIntValue();
                        continue;
                    }
                }else if (JSON_TARGET_URL.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        targetUrl =parser.getText();
                        continue;
                    }
                }else if (JSON_CAMPAIGNURL.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        campaignUrl=parser.getText();
                        continue;
                    }
                }else if (JSON_THEMEICONKEY.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        themeIconKey =parser.getText();
                        continue;
                    }
                }else if (JSON_NEWFLAGVERSION.equals(fieldName)) {
                    if (token == JsonToken.VALUE_NUMBER_INT) {
                        newFlagVersion =parser.getIntValue();
                        continue;
                    }
                }else if (JSON_CHANNELID.equals(fieldName)) {
                    if (token == JsonToken.VALUE_NUMBER_INT) {
                        channelId =parser.getIntValue();
                        continue;
                    }
                }else if (JSON_BADGETYPE.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        badgeType =parser.getText();
                        continue;
                    }
                }else if (JSON_ICONN_URL_BASE.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        iconUrlBase = parser.getText();
                        continue;
                    }
                }else if (JSON_MARKET_URL.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        marketUrl =parser.getText();
                        continue;
                    }
                }else if (JSON_UPPER_BOUNDSPEC.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        upperBoundSpec =parser.getText();
                        continue;
                    }
                }else if (JSON_PACKAGE_NAME.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        packageName =parser.getText();
                        continue;
                    }
                }else if (JSON_TITLE.equals(fieldName)) {
                    if (token == JsonToken.VALUE_STRING) {
                        title =parser.getText();
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
        sb.append("{ " + JSON_ID + ": ");
        sb.append(id);
        sb.append(", " + JSON_VERSION + ": ");
        sb.append(version);
        sb.append(", " + JSON_SUTYPE + ": ");
        sb.append(subType);
        sb.append(", " + JSON_TARGET_URL + ": ");
        sb.append(targetUrl);
        sb.append(", " + JSON_CAMPAIGNURL + ": ");
        sb.append(campaignUrl);
        sb.append(", " + JSON_THEMEICONKEY + ": ");
        sb.append(themeIconKey);
        sb.append(", " + JSON_NEWFLAGVERSION + ": ");
        sb.append(newFlagVersion);
        sb.append(", " + JSON_ICONN_URL_BASE + ": ");
        sb.append(iconUrlBase);
        sb.append(", " + JSON_MARKET_URL + ": ");
        sb.append(marketUrl);
        sb.append(", " + JSON_PACKAGE_NAME + ": ");
        sb.append(packageName);
        sb.append(", " + JSON_SPEC + ": ");
        sb.append(spec);
        sb.append(", " + JSON_UPPER_BOUNDSPEC + ": ");
        sb.append(upperBoundSpec);
        sb.append(", " + JSON_TITLE + ": ");
        sb.append(title);

        sb.append(" }");
        return sb.toString();
    }
}

