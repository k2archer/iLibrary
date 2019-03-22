package com.kwei.ilibrary.comm.ViewAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.kwei.ilibrary.R;

import java.util.List;

public class MultiTypeItemAdapter<ItemType> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_EMPTY = -1;

    protected List<ItemType> mData;
    protected Context mContext;
    protected ItemViewDelegateManager<ItemType> mItemDelegateManger;

    public MultiTypeItemAdapter(Context context, List<ItemType> data) {
        mData = data;
        mContext = context;
        mItemDelegateManger = new ItemViewDelegateManager<>();
    }

    @Override
    public int getItemViewType(int position) {
        int count = mData == null ? 0 : mData.size();
        if (count == 0) {
            return TYPE_EMPTY;
        }
        return ((BaseItem) mData.get(position)).getItemViewType();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_EMPTY:
                return ViewHolder.createViewHolder(mContext, parent, R.layout.complex_empty_view);
            default:
                ItemViewDelegate itemDelegate = mItemDelegateManger.getDelegate(viewType);
                int layoutId = itemDelegate.getItemLayoutId();
                ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
                // created then do something ... such as setListener
                return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        convert(viewHolder, mData.get(i));
    }

    public MultiTypeItemAdapter addItemDelegate(ItemViewDelegate<ItemType> delegate) {
        mItemDelegateManger.addDelegate(delegate.getViewType(), delegate);
        return this;
    }

    private void convert(RecyclerView.ViewHolder holder, ItemType item) {
        mItemDelegateManger.convert(holder, item, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
