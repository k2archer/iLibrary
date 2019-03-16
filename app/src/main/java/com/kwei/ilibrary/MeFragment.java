package com.kwei.ilibrary;

import android.widget.TextView;

import com.kwei.ilibrary.base.BaseFragment;

public class MeFragment extends BaseFragment {

    private TextView EdUserName;

    @Override
    public int bindLayout() {
        return R.layout.fragment_me;
    }

    @Override
    public void onCreateView() {
        initView();
    }

    private void initView() {
        EdUserName = mRootView.findViewById(R.id.user_name_tv);
        EdUserName.setText(DataManager.getInstance().getUserName());
    }
}
