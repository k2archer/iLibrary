package com.kwei.ilibrary.util;

import com.kwei.ilibrary.config.ConfigHelper;

public class ServerURL {
    static final String protocol = "http:";
    static final String version = "v.1.0";

    public static String getLoginURL(String name, String password) {
        return getHostURL() + "user/login.cgi?user=" + name + "&pass=" + password;
    }

    private static String getHostURL() {
        String host = ConfigHelper.read(ConfigHelper.PreferencesKey.HOST_ADDRESS);
        return protocol + "//" + host + "/service/" + version + "/";
    }
}