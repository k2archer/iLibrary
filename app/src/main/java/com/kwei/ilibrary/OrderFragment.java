package com.kwei.ilibrary;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kwei.ilibrary.base.BaseFragment;
import com.kwei.ilibrary.comm.EventBus.EventBus;
import com.kwei.ilibrary.comm.EventBus.Subscriber;
import com.kwei.ilibrary.comm.EventTag;
import com.kwei.ilibrary.comm.RecyclerViewAdapter;
import com.kwei.ilibrary.util.BookItemData;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends BaseFragment {

    private List<BookItemData> mOrderedListData = new ArrayList<>();
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

    private Subscriber<List<BookItemData>> mOrderedSubscriber = new Subscriber<List<BookItemData>>() {
        @Override
        public void onEvent(List<BookItemData> event) {
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
