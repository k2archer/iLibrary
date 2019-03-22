package com.kwei.ilibrary.comm;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kwei.ilibrary.R;
import com.kwei.ilibrary.util.BookItemData;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_EMPTY = -1;
    private List<BookItemData> mData;

    public RecyclerViewAdapter(List<BookItemData> data) {
        mData = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY:
                return new EmptyViewHolder(parent);
            default:
                return new ItemViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            BookItemData book = mData.get(position);
            ((ItemViewHolder) holder).textView.setText(book.name);
        }
    }

    @Override
    public final int getItemCount() {
        return getCount() == 0 ? 1 : getCount();
    }

    private int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (getCount() == 0) {
            return TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ItemViewHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_lsit_item, parent, false));
        }

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_book_name);
        }

    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(ViewGroup parent) {
            this(LayoutInflater.from(parent.getContext()).inflate(R.layout.complex_empty_view, parent, false));
        }

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}