package com.kwei.ilibrary;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kwei.ilibrary.base.BaseFragment;
import com.kwei.ilibrary.comm.EventBus.EventBus;
import com.kwei.ilibrary.comm.EventBus.Subscriber;
import com.kwei.ilibrary.comm.EventTag;
import com.kwei.ilibrary.comm.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private RecyclerView mRecommendedList;
    private List<String> mRecommendedListData;
    private RecyclerViewAdapter mAdapter;

    MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onCreateView() {
        mRecommendedList = mRootView.findViewById(R.id.home_recommend_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRootView.getContext());
        mRecommendedList.setLayoutManager(layoutManager);
        mRecommendedListData = new ArrayList<>();
        mAdapter = new RecyclerViewAdapter(mRecommendedListData);
        mRecommendedList.setAdapter(mAdapter);
        getRecommendedList();
    }

    private void getRecommendedList() {
        EventBus.getInstance().register(new Subscriber<List<String>>() {

            @Override
            public void onEvent(List<String> event) {
                mRecommendedListData.addAll(event);
                mAdapter.notifyDataSetChanged();
            }
        }, new EventTag(EventTag.RECOMMENDED_LIST));

        DataManager.getInstance().getRecommendedList();
    }
}