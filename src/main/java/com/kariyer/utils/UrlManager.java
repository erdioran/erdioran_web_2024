package com.kariyer.utils;


import static com.kariyer.utils.ConfigManager.getTestUrlConfig;

public class UrlManager {


    public static String getTestUrl() {
        String env = getTestUrlConfig();
        String siteUrl = null;

        if (env.equals("prod")) {
            siteUrl = DataManager.getData("url.base");
        } else {
            siteUrl = "http://" + env + ".kariyer.net";
        }

        return siteUrl;
    }


}
