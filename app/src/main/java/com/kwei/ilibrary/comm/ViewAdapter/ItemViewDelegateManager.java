package com.kwei.ilibrary.comm.ViewAdapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

public class ItemViewDelegateManager<ItemType> {
    private SparseArray<ItemViewDelegate<ItemType>> mDelegates = new SparseArray<>();

    public ItemViewDelegateManager<ItemType> addDelegate(ItemViewDelegate<ItemType> delegate) {
        if (delegate != null) {
            int viewType = mDelegates.size();
            mDelegates.put(viewType, delegate);
        }
        return this;
    }

    public ItemViewDelegateManager<ItemType> addDelegate(int viewType, ItemViewDelegate<ItemType> delegate) {
        if (mDelegates.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "This delegate(" + mDelegates.get(viewType) + ")is registered ");
        }
        mDelegates.put(viewType, delegate);
        return this;
    }

    public void convert(RecyclerView.ViewHolder holder, ItemType item, int position) {
        int itemType = ((BaseItem) item).getItemViewType();
        int delegateCount = mDelegates.size();
        for (int i = 0; i < delegateCount; i++) {
            ItemViewDelegate<ItemType> itemDelegate = mDelegates.valueAt(i);
            if (itemType == itemDelegate.getViewType()) {
                itemDelegate.convert(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException(
                "The " + position + " Item has no match delegate.");
    }

    public ItemViewDelegate getDelegate(int viewType) {
        return mDelegates.get(viewType);
    }

}
