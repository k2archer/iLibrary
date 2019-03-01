package com.kwei.ilibrary;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kwei.ilibrary.base.BaseActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
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
                break;
            case R.id.ed_user_name:

                break;
            case R.id.ed_user_password:

                break;
        }
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }
}
