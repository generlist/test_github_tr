package com.linecorp.menu.validate.data;


import java.util.Arrays;
import java.util.List;


public class StaticMenuMeta {

    public static class LanguageItem {

        public String languageCode;
        public String title;

        public LanguageItem(String title, String languageCode) {

            this.title = title;
            this.languageCode = languageCode;

        }

        public static  List<LanguageItem> Language_LIST =  Arrays.asList(new LanguageItem("영어", "en"), new LanguageItem("한국어", "kr"), new LanguageItem("일본어 ", "jp"));


    }

    public static class DeviceItem {

        public String title;
        public String device;

        public DeviceItem(String title, String device) {
            this.title = title;
            this.device = device;

        }

        public static  List<DeviceItem> DEVICE_ITEM_LIST = Arrays.asList(new DeviceItem("아이폰", "iphone"), new DeviceItem("안드로이드", "android"));

    }

    public static class RegionItem {

        public String title;
        public String region;

        private RegionItem(String title, String region) {

            this.title = title;
            this.region = region;

        }

        public static final List<RegionItem> Region_LIST = Arrays.asList(new RegionItem("인도네시아", "id"), new RegionItem("한국", "kr"), new RegionItem("미국", "en"), new RegionItem("일본", "jp"));


    }

    public static class ServerTypeItem {

        public String serverType;
        public String serverName;

        public ServerTypeItem(String serverType,String serverName) {

            this.serverType = serverType;
            this.serverName = serverName;

        }

        public static  List<ServerTypeItem> serverType_LIST =  Arrays.asList(new ServerTypeItem("BETA", "http://appresources.beta.line.naver.jp/moretab/list.json?lang=%s&device=%s&region=%s&v=%s"),
                                                                             new ServerTypeItem("RC", "http://appresources.rc.line.naver.jp/moretab/list.json?lang=%s&device=%s&region=%s&v=%s"),
                                                                             new ServerTypeItem("REAL", "http://appresources.line.naver.jp/moretab/list.json?lang=%s&device=%s&region=%s&v=%s"));

    }

}

