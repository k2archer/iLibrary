package com.kwei.ilibrary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kwei.ilibrary.base.BaseActivity;
import com.kwei.ilibrary.base.BaseApplication;
import com.kwei.ilibrary.util.HttpCallbackListener;
import com.kwei.ilibrary.util.HttpUtil;
import com.kwei.ilibrary.util.ResponseBody;
import com.kwei.ilibrary.util.ServerURL;

public class LoginActivity extends BaseActivity {
    private Button BtLogin;
    private EditText EdUserName;
    private EditText EdUserPassword;

    @Override
    public void initView(View view) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide(); // 隐藏标题栏

        BtLogin = $(R.id.bt_login);
        EdUserName = $(R.id.ed_user_name);
        EdUserPassword = $(R.id.ed_user_password);

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
                Log.d(TAG, "onFailure: " + e.toString());
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
