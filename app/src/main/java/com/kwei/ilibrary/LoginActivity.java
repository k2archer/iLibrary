package com.kwei.ilibrary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kwei.ilibrary.base.BaseActivity;
import com.kwei.ilibrary.base.BaseApplication;
import com.kwei.ilibrary.config.ConfigHelper;
import com.kwei.ilibrary.util.HttpCallbackListener;
import com.kwei.ilibrary.util.HttpUtil;
import com.kwei.ilibrary.util.LogUtil;
import com.kwei.ilibrary.util.ResponseBody;
import com.kwei.ilibrary.util.ServerURL;

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
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                login(EdUserName.getText().toString(), EdUserPassword.getText().toString());
                break;
            case R.id.ed_user_name:

                break;
            case R.id.ed_user_password:

                break;
        }
    }

    private void login(String name, String password) {
        String loginURL = ServerURL.getLoginURL(name, password);
        HttpUtil.sendGETRequest(loginURL, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                ResponseBody body = ResponseBody.parse(response);
                if (body.code == ResponseBody.SUCCEED) {
                    Context context = BaseApplication.getInstance().getApplicationContext();
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Exception e) {
                LogUtil.d(e.toString());
                Toast(e.toString());
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
