package com.kwei.ilibrary;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kwei.ilibrary.base.BaseActivity;
import com.kwei.ilibrary.config.ConfigHelper;

public class LoginActivity extends BaseActivity {
    private Button BtLogin;
    private EditText EdUserName;
    private EditText EdUserPassword;

    private AlertDialog mDialog;

    @Override
    public void initView(View view) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide(); // 隐藏标题栏

        BtLogin = $(R.id.bt_login);
        EdUserName = $(R.id.ed_user_name);
        EdUserPassword = $(R.id.ed_user_password);

        TextView TvSetting = $(R.id.tv_setting);
        TvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginSettingDialog();
            }
        });

    }

    private void showLoginSettingDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.setting_dialog, null, false);
        EditText hostAddress = dialogView.findViewById(R.id.host_address_et);
        String host = ConfigHelper.read(ConfigHelper.PreferencesKey.HOST_ADDRESS);
        hostAddress.setText(host);
        dialogView.findViewById(R.id.positive_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText host_address = dialogView.findViewById(R.id.host_address_et);
                ConfigHelper.save(ConfigHelper.PreferencesKey.HOST_ADDRESS, host_address.getText().toString());
                mDialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.negative_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        builder.setView(dialogView);
        mDialog = builder.create();
        mDialog.show();
    }

    @Override
    public void setListener() {
        BtLogin.setOnClickListener(this);
        EdUserName.setOnClickListener(this);
        EdUserPassword.setOnClickListener(this);
        EdUserPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                }
                return false;
            }
        });
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.ed_user_name:

                break;
            case R.id.ed_user_password:

                break;
        }
    }

    private void login() {
        login(EdUserName.getText().toString(), EdUserPassword.getText().toString());
    }

    private void login(final String user_name, String password) {
        if (user_name.length() == 0 || password.length() == 0) {
            Toast("用户名和密码不能为空");
            return;
        }

        DataManager.getInstance().login(user_name, password, new DataCallback() {
            @Override
            public void onSucceed(Object result) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user_name", user_name);
                startActivity(intent);
            }

            @Override
            public void onFailed(final Object result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast(result.toString());
                    }
                });
            }
        });
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_login;
    }
}
