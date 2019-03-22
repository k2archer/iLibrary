package com.kwei.ilibrary.util;

import android.support.v7.widget.RecyclerView;

import com.kwei.ilibrary.R;
import com.kwei.ilibrary.comm.ViewAdapter.BaseItem;
import com.kwei.ilibrary.comm.ViewAdapter.ItemViewDelegate;
import com.kwei.ilibrary.comm.ViewAdapter.ViewHolder;

public class BookItemDelegate<ItemType extends BaseItem> implements ItemViewDelegate<ItemType> {
    @Override
    public int getItemLayoutId() {
        return R.layout.recommended_lsit_item;
    }

    @Override
    public int getViewType() {
        return BookItemData.TYPE;
    }

    @Override
    public void convert(RecyclerView.ViewHolder holder, ItemType item, int position) {
        BookItemData bookItem = (BookItemData) item;
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setTextView(R.id.tv_book_name, bookItem.name);
        String url = ServerURL.getImageURL(bookItem.code);
        viewHolder.setImageView(R.id.iv_book_cover, url);
    }
}
