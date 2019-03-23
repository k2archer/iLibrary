package com.kwei.ilibrary.util;

import com.kwei.ilibrary.comm.ViewAdapter.BaseItem;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderedItemData extends BaseItem {
    public static final int TYPE = 102;
    public String name;
    public String code;

    public OrderedItemData(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static OrderedItemData create(JSONObject book_json) {

        OrderedItemData bookItem = null;
        try {
            String name = book_json.getString("book_name");
            String cover = book_json.getString("book_code");
            bookItem = new OrderedItemData(name, cover);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bookItem;
    }

    @Override
    public int getItemViewType() {
        return TYPE;
    }
}
