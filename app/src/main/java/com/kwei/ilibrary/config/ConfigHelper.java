package com.kwei.ilibrary.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.kwei.ilibrary.base.BaseApplication;


public class ConfigHelper {
    private static final String CONFIG_FILE_NAME = "base_config";

    public enum PreferencesKey {
        HOST_ADDRESS;

        public static String to(PreferencesKey key) {
            switch (key) {
                case HOST_ADDRESS:
                    return "host_address";
            }
            return "";
        }
    }

    public static void save(PreferencesKey key, String text) {
        Context context = BaseApplication.getInstance().getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PreferencesKey.to(key), text);
        editor.apply();
    }

    public static String read(PreferencesKey key) {
        Context context = BaseApplication.getInstance().getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PreferencesKey.to(key), "");
    }


}
