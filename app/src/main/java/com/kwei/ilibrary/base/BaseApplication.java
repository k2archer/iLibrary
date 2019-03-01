package com.kwei.ilibrary.base;

import android.app.Application;

public class BaseApplication extends Application {
    private static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        // 初始化自定义 Crash 处理器
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this, false);
    }

    public static BaseApplication getInstance() {
        return sInstance;
    }
}
