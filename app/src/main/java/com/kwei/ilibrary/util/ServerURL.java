package com.kwei.ilibrary.util;

import com.kwei.ilibrary.config.ConfigHelper;

public class ServerURL {
    static final String protocol = "http:";
    static final String version = "v.1.0";

    public static String getLoginURL(String name, String password) {
        String host = ConfigHelper.read(ConfigHelper.PreferencesKey.HOST_ADDRESS);
        String api = protocol + "//" + host + "/service/" + version + "/";
        return api + "user/login.cgi?user=" + name + "&pass=" + password;
    }

}