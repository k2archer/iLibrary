package com.kwei.ilibrary;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kwei.ilibrary.base.BaseFragment;
import com.kwei.ilibrary.comm.EventBus.EventBus;
import com.kwei.ilibrary.comm.EventBus.Subscriber;
import com.kwei.ilibrary.comm.EventTag;
import com.kwei.ilibrary.comm.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends BaseFragment {

    private List<String> mOrderedListData = new ArrayList<>();
    private RecyclerViewAdapter mOrderedAdapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_ordered;
    }

    @Override
    public void onCreateView() {

        initView();
        getOrderedList();
    }

    private void initView() {
        RecyclerView mOrderedList = mRootView.findViewById(R.id.ordered_list);
        mOrderedList.setLayoutManager(new LinearLayoutManager(mRootView.getContext()));
        mOrderedAdapter = new RecyclerViewAdapter(mOrderedListData);
        mOrderedList.setAdapter(mOrderedAdapter);
    }

    private Subscriber<List<String>> mOrderedSubscriber = new Subscriber<List<String>>() {
        @Override
        public void onEvent(List<String> event) {
            mOrderedListData.clear();
            mOrderedListData.addAll(event);
            mOrderedAdapter.notifyDataSetChanged();
        }
    };

    private void getOrderedList() {
        EventBus.getInstance().register(mOrderedSubscriber, new EventTag(EventTag.ORDERED_LIST));
        DataManager.getInstance().getOrderedList();
    }
}
