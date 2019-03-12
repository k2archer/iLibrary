package com.kwei.ilibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected final String TAG = this.getClass().getSimpleName();
    private View mContextView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View mView = bindView();
        if (null == mView) {
            mContextView = LayoutInflater.from(this).inflate(bindLayout(), null);
        } else {
            mContextView = mView;
        }
        setContentView(mContextView);
        initView(mContextView);

        setListener();

    }

    // 绑定控件
    protected <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }

    public abstract void initView(final View view);

    public abstract void setListener();

    public abstract void widgetClick(View v);

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    public abstract int bindLayout();

    public abstract View bindView();

    public void Toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
