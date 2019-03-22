package com.kwei.ilibrary.util;

import com.kwei.ilibrary.comm.ViewAdapter.BaseItem;

import org.json.JSONException;
import org.json.JSONObject;

public class BookItemData extends BaseItem {
    public static final int TYPE = 101;
    public String name;
    public String code;

    public BookItemData(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static BookItemData create(JSONObject book_json) {

        BookItemData bookItem = null;
        try {
            String name = book_json.getString("book_name");
            String cover = book_json.getString("book_code");
            bookItem = new BookItemData(name, cover);
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
