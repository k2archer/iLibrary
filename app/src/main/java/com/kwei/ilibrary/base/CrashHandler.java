package com.kwei.ilibrary.base;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final CrashHandler sInstance = new CrashHandler();
    }

    public void init(Context context, boolean useDefaultHandler) {
        if (useDefaultHandler) {
            mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        }
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        if (!handleException(throwable) && mDefaultCrashHandler != null) {
            // 如果没有自定义处理器则让系统默认的异常处理器来处理
            mDefaultCrashHandler.uncaughtException(thread, throwable);
        } else {
            showDelayToast();
            crashRestartApp(mContext, CrashDialogActivity.class, 1000);  // 重启 App
        }
    }

    private boolean handleException(Throwable throwable) {
        if (throwable == null) return false;

        // export trace info
        try {
            dumpExceptionToSDCard(throwable);
            // todo ... upload to network
            // dumpExceptionToServer(throwable);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void showDelayToast() {
        int delayTime = 1; // 延时重启时间(秒)
        final String message = "很抱歉，程序出现异常，" + delayTime + "秒后重启";
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();

        try {
            Thread.sleep(delayTime * 1000);
        } catch (InterruptedException e) {
            Log.e(TAG, "error : " + e.getMessage());
        }
    }

    private static void crashRestartApp(Context context, Class cls, long delayMillis) {
        Intent intent = new Intent(context, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // 延迟 delayMillis 毫秒执行操作
        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + delayMillis, restartIntent);
        android.os.Process.killProcess(android.os.Process.myPid());  // 退出程序
        System.exit(1);
    }

    private void dumpExceptionToSDCard(Throwable throwable) throws IOException {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        // 拼接 Crash info 保存文件路径
        String PACKAGE_NAME = mContext.getPackageName();
        String appName = PACKAGE_NAME.substring(PACKAGE_NAME.lastIndexOf('.') + 1);
        String CRASH_FILE_PATH =
                Environment.getExternalStorageDirectory().getPath() + "/" + appName + "/log/";

        File directory = new File(CRASH_FILE_PATH);
        if (!directory.exists())
            directory.mkdirs();

        Date date = new Date(System.currentTimeMillis());
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        File file = new File(CRASH_FILE_PATH + "/" + appName + "_crash " + time + ".trace");

        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));

        try {
            writer.println(time);
            getPhoneInfo(writer);
            writer.println();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        throwable.printStackTrace(writer);
        writer.close();
    }

    private void getPhoneInfo(PrintWriter writer) throws PackageManager.NameNotFoundException {
        PackageManager manager = mContext.getPackageManager();
        PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        String appVersion = "App Version: " + info.versionName + "_" + info.versionCode;
        writer.println(appVersion);

        String OSVersion = "OS Version: " + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK;
        writer.println(OSVersion);

        // 手机的制造商、型号、架构
        writer.println("Vendor:" + Build.MANUFACTURER);
        writer.println("Model: " + Build.MODEL);
        writer.println("CPU ARI: " + Build.CPU_ABI);
    }
}
