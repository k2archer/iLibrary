package com.kwei.ilibrary;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kwei.ilibrary.base.BaseActivity;

public class MainActivity extends BaseActivity {
    @Override
    public void setListener() {
        $(R.id.menu_home_bt).setOnClickListener(this);
        $(R.id.menu_home_bt).performClick();

        $(R.id.menu_borrow_bt).setOnClickListener(this);
        $(R.id.menu_message_bt).setOnClickListener(this);
    }

    FragmentManager mManager = getSupportFragmentManager();
    private String mCurrentFragmentTag;

    private void initFragment() {
        FragmentTransaction transaction = mManager.beginTransaction();
        transaction.add(R.id.frame_content, new MessageFragment(), String.valueOf(R.id.menu_message_bt));
        transaction.add(R.id.frame_content, new OrderFragment(), String.valueOf(R.id.menu_borrow_bt));
        transaction.add(R.id.frame_content, new HomeFragment(), String.valueOf(R.id.menu_home_bt));
        mCurrentFragmentTag = String.valueOf(R.id.menu_home_bt);

        transaction.commit();
        mManager.executePendingTransactions();

    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.menu_home_bt:
            case R.id.menu_borrow_bt:
            case R.id.menu_message_bt:
                Fragment fragment = mManager.findFragmentByTag(String.valueOf(v.getId()));
                if (fragment != null && !mCurrentFragmentTag.equals(fragment.getTag())) {
                    Fragment currentFragment = mManager.findFragmentByTag(mCurrentFragmentTag);
                    FragmentTransaction transaction = mManager.beginTransaction();
                    if (currentFragment != null) transaction.hide(currentFragment);
                    transaction.show(fragment).commit();
                    mManager.executePendingTransactions();
                    mCurrentFragmentTag = fragment.getTag();
                }
                break;
        }

    }

    @Override
    public void initView(View view) {
        initFragment();

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
