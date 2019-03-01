package com.kwei.ilibrary.util;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseBody {
    public int code;
    public String message;
    public String result;
    public static final int SUCCEED = 200;
    public static final int FAILED = 500;

    public static ResponseBody parse(String json) {
        ResponseBody response = new ResponseBody();
        try {
            JSONObject jsonObject = new JSONObject(json);
            response.code = jsonObject.getInt("code");
            response.message = jsonObject.getString("message");
            response.result = jsonObject.getString("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;
    }
}
