package com.kwei.ilibrary;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kwei.ilibrary.base.BaseFragment;
import com.kwei.ilibrary.comm.EventBus.EventBus;
import com.kwei.ilibrary.comm.EventBus.Subscriber;
import com.kwei.ilibrary.comm.EventTag;
import com.kwei.ilibrary.comm.RecyclerViewAdapter;
import com.kwei.ilibrary.util.BookItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private List<BookItem> mOrderedListData = new ArrayList<>();
    private RecyclerViewAdapter mOrderedAdapter;
    private List<BookItem> mRecommendedListData = new ArrayList<>();
    private RecyclerViewAdapter mRecommendedAdapter;

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
        initView();
        getOrderedList();
        getRecommendedList();
    }

    private void initView() {
        RecyclerView mRecommendedList = mRootView.findViewById(R.id.home_recommend_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRootView.getContext());
        mRecommendedList.setLayoutManager(layoutManager);
        mRecommendedAdapter = new RecyclerViewAdapter(mRecommendedListData);
        mRecommendedList.setAdapter(mRecommendedAdapter);

        RecyclerView mOrderedList = mRootView.findViewById(R.id.home_ordered_list);
        mOrderedList.setLayoutManager(new LinearLayoutManager(mRootView.getContext()));
        mOrderedAdapter = new RecyclerViewAdapter(mOrderedListData);
        mOrderedList.setAdapter(mOrderedAdapter);
    }

    private Subscriber<List<BookItem>> mOrderedSubscriber = new Subscriber<List<BookItem>>() {
        @Override
        public void onEvent(List<BookItem> event) {
            mOrderedListData.clear();
            mOrderedListData.addAll(event);
            mOrderedAdapter.notifyDataSetChanged();
        }
    };

    private void getOrderedList() {
        EventBus.getInstance().register(mOrderedSubscriber, new EventTag(EventTag.ORDERED_LIST));
        DataManager.getInstance().getOrderedList();
    }

    private Subscriber<List<BookItem>> mRecommendedSubscriber = new Subscriber<List<BookItem>>() {

        @Override
        public void onEvent(List<BookItem> event) {
            mRecommendedListData.clear();
            mRecommendedListData.addAll(event);
            mRecommendedAdapter.notifyDataSetChanged();
        }
    };

    private void getRecommendedList() {
        EventBus.getInstance().register(mRecommendedSubscriber, new EventTag(EventTag.RECOMMENDED_LIST));
        DataManager.getInstance().getRecommendedList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unregister(mRecommendedSubscriber);
        EventBus.getInstance().unregister(mOrderedSubscriber);
    }
}