package com.linecorp.menu.validate.data;



public class MenuParameterItem {


    public String serverName;
    public String languageCode;
    public String device;
    public String region;

    public MenuParameterItem(String serverName,String languageCode, String device , String region){
       this.serverName = serverName;
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


