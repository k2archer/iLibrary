package com.kwei.ilibrary;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kwei.ilibrary.base.BaseFragment;
import com.kwei.ilibrary.comm.EventBus.EventBus;
import com.kwei.ilibrary.comm.EventBus.Subscriber;
import com.kwei.ilibrary.comm.EventTag;
import com.kwei.ilibrary.comm.RecyclerViewAdapter;
import com.kwei.ilibrary.comm.ViewAdapter.BaseItem;
import com.kwei.ilibrary.comm.ViewAdapter.MultiTypeItemAdapter;
import com.kwei.ilibrary.util.BookItemData;
import com.kwei.ilibrary.util.BookItemDelegate;
import com.kwei.ilibrary.util.LogUtil;
import com.kwei.ilibrary.util.OrderedItemData;
import com.kwei.ilibrary.util.OrderedItemDelegate;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends BaseFragment {

    private List<BaseItem> mOrderedListData = new ArrayList<>();
    private MultiTypeItemAdapter mOrderedAdapter;

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
        //添加Android自带的分割线
        mOrderedList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        mOrderedAdapter = new MultiTypeItemAdapter<>(getActivity(), mOrderedListData);
        mOrderedAdapter.addItemDelegate(new OrderedItemDelegate<OrderedItemData>());
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
