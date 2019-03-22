package com.kwei.ilibrary.util;

import org.json.JSONException;
import org.json.JSONObject;

public class BookItem {
    public String name;
    public String cover;

    public BookItem(String name, String cover) {
        this.name = name;
        this.cover = cover;
    }

    public static BookItem create(JSONObject book_json) {

        BookItem bookItem = null;
        try {
            String name = book_json.getString("book_name");
            String cover = book_json.getString("book_code");
            bookItem = new BookItem(name, cover);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bookItem;
    }
}
