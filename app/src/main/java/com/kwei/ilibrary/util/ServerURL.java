package com.kwei.ilibrary.util;

import com.kwei.ilibrary.config.ConfigHelper;

public class ServerURL {
    static final String protocol = "http:";
    static final String version = "v.1.0";

    public static String getLoginURL(String name, String password) {
        return getHostURL() + "user/login.cgi?user=" + name + "&pass=" + password;
    }

    public static String getImageURL(String cover) {
        String host = ConfigHelper.read(ConfigHelper.PreferencesKey.HOST_ADDRESS);
        return protocol + "//" + host + "/files/image/" + cover + ".jpg";
    }

    public static String getOrderURL(String name) {
        return getHostURL() + "user/order.cgi?user=" + name;
    }

    public static String getRecommendedListURL(String name) {
        return getHostURL() + "user/recommended.cgi?user=" + name;
    }

    private static String getHostURL() {
        String host = ConfigHelper.read(ConfigHelper.PreferencesKey.HOST_ADDRESS);
        return protocol + "//" + host + "/service/" + version + "/";
    }
}