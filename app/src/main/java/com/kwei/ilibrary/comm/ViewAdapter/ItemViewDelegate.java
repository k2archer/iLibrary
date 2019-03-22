package com.kwei.ilibrary.comm.ViewAdapter;

import android.support.v7.widget.RecyclerView;

public interface ItemViewDelegate<ItemType> {
    int getItemLayoutId();

    int getViewType();

    void convert(RecyclerView.ViewHolder holder, ItemType item, int position);
}
