package com.kwei.ilibrary;

import android.content.Context;
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

public class HomeFragment extends BaseFragment {

    private List<BaseItem> mOrderedListData = new ArrayList<>();
    private MultiTypeItemAdapter mOrderedAdapter;
    private List<BaseItem> mRecommendedListData = new ArrayList<>();
    private MultiTypeItemAdapter mRecommendedAdapter;

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
        //添加Android自带的分割线
        mRecommendedList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        mRecommendedAdapter = new MultiTypeItemAdapter<>(getActivity(), mRecommendedListData);
        mRecommendedAdapter.addItemDelegate(new BookItemDelegate<>());
        mRecommendedList.setAdapter(mRecommendedAdapter);

        RecyclerView mOrderedList = mRootView.findViewById(R.id.home_ordered_list);
        mOrderedList.setLayoutManager(new LinearLayoutManager(mRootView.getContext()));
        //添加Android自带的分割线
        mOrderedList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

//        mOrderedAdapter = new RecyclerViewAdapter(mOrderedListData);
        mOrderedAdapter = new MultiTypeItemAdapter<>(getActivity(), mOrderedListData);
        mOrderedAdapter.addItemDelegate(new OrderedItemDelegate<>());
        mOrderedList.setAdapter(mOrderedAdapter);
    }

    private Subscriber<List<BaseItem>> mOrderedSubscriber = new Subscriber<List<BaseItem>>() {
        @Override
        public void onEvent(List<BaseItem> event) {
            mOrderedListData.clear();
            mOrderedListData.addAll(event);
            mOrderedAdapter.notifyDataSetChanged();
        }
    };

    private void getOrderedList() {
        EventBus.getInstance().register(mOrderedSubscriber, new EventTag(EventTag.ORDERED_LIST));
        DataManager.getInstance().getOrderedList();
    }

    private Subscriber<List<BaseItem>> mRecommendedSubscriber = new Subscriber<List<BaseItem>>() {

        @Override
        public void onEvent(List<BaseItem> event) {
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