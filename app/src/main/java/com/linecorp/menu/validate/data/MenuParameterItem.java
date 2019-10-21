package com.linecorp.menu.validate.data;



public class MenuParameterItem {


    public String languageCode;
    public String device;
    public String region;

    public MenuParameterItem(String languageCode, String device , String region){
        this.languageCode=languageCode;
        this.device = device;
        this.region =region;
    }

    public enum MenuParamType {

        Language,
        Device,
        Region,

    }
}


