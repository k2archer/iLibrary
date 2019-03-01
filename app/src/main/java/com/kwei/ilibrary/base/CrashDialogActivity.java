package com.kwei.ilibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.kwei.ilibrary.R;

public class CrashDialogActivity extends Activity {
    private static final String DIALOG_TITLE = "dialog_title";
    private static final String DIALOG_MESSAGE = "dialog_message";

    private String dialogTitle, dialogMessage;

    public static Intent newIntent(Context context, String title, String message) {
        Intent intent = new Intent();
        intent.setClass(context, CrashDialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(DIALOG_TITLE, title);
        intent.putExtra(DIALOG_MESSAGE, message);
        return intent;
    }

    private void parseIntent(Intent intent) {
        dialogTitle = intent.getStringExtra(DIALOG_TITLE);
        dialogMessage = intent.getStringExtra(DIALOG_MESSAGE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        parseIntent(getIntent());
        if (dialogMessage == null) {
            dialogMessage = getString(R.string.dialog_default_message);
        }
        if (dialogTitle == null) {
            dialogTitle = getApplicationName(this);
        }
        showDialog();
    }

    private String getApplicationName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String name;
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(
                    context.getApplicationInfo().packageName, 0);
            name = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (final PackageManager.NameNotFoundException e) {
            String[] packages = context.getPackageName().split(".");
            name = packages[packages.length - 1];
        }
        return name;
    }

    private void showDialog() {
        new AlertDialog.Builder(this)
                .setTitle(dialogTitle)
                .setMessage(dialogMessage)
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                })
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setPositiveButton("重启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        restartApp();
                    }
                }).show();
    }

    private void restartApp() {
        PackageManager manager = getBaseContext().getPackageManager();
        Intent intent = manager.getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
