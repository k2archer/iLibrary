package com.kwei.ilibrary.util;

public class ServerURL {
    static final String protocol = "http:";
    static final String host = "192.168.0.153:8180";
    static final String version = "v.1.0";
    static final String api = protocol + "//" + host + "/service/" + version + "/";

    public static String getLoginURL(String name, String password) {
        return api + "user/login.cgi?user=" + name + "&pass=" + password;
    }

}