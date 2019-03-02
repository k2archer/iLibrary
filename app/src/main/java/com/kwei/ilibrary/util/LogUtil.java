package com.kwei.ilibrary.util;

import android.text.TextUtils;
import android.util.Log;

import com.kwei.ilibrary.BuildConfig;


public class LogUtil {
    public static String tagPrefix = "";
    private static boolean IS_DEBUG = BuildConfig.DEBUG;

    public static void i(String message) {
        if (IS_DEBUG) {
            Log.i(generateTag() + "-->:", message);
        }
    }

    public static void e(String message) {
        if (IS_DEBUG) {
            Log.e(generateTag() + "-->", message);
        }
    }

    public static void w(String message) {
        if (IS_DEBUG) {
            Log.w(generateTag() + "-->:", message);
        }
    }

    public static void v(String message) {
        if (IS_DEBUG) {
            Log.v(generateTag() + "-->:", message);
        }
    }

    public static void d(String message) {
        if (IS_DEBUG) {
            Log.d(generateTag() + "-->", message);
        }
    }

    private static String generateTag() {
        // 从堆栈信息中获取当前被调用的方法信息, 获取堆栈信息会影响性能
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        String callerClazzName = stackTraceElement.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        String tag = "%s.%s(" + stackTraceElement.getFileName() + ":%d)";
        tag = String.format(tag, new Object[]{callerClazzName, stackTraceElement.getMethodName(),
                Integer.valueOf(stackTraceElement.getLineNumber())});
        // 给 tag 设置前缀
        tag = TextUtils.isEmpty(tagPrefix) ? tag : tagPrefix + ":" + tag;
        return tag;
    }
}
