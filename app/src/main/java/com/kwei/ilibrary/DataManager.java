package com.kwei.ilibrary;

import com.kwei.ilibrary.comm.EventBus.EventBus;
import com.kwei.ilibrary.comm.EventTag;
import com.kwei.ilibrary.util.HttpCallbackListener;
import com.kwei.ilibrary.util.HttpUtil;
import com.kwei.ilibrary.util.LogUtil;
import com.kwei.ilibrary.util.ResponseBody;
import com.kwei.ilibrary.util.ServerURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private DataManager() {
    }

    private static class Instance {
        private static final DataManager sInstance = new DataManager();
    }

    private String mUserName;

    public static DataManager getInstance() {
        return Instance.sInstance;
    }

    public void login(final String name, String password, final DataCallback callback) {
        if (name.length() == 0 || password.length() == 0) {
            callback.onFailed("用户名和密码不能为空");
            return;
        }
        String loginURL = ServerURL.getLoginURL(name, password);
        HttpUtil.sendGETRequest(loginURL, new HttpCallbackListener() {

            @Override
            public void onFinish(String response) {
                ResponseBody body = ResponseBody.parse(response);
                if (body == null || body.code == ResponseBody.ERROR) {
                    callback.onFailed("登录失败");
                    LogUtil.d("onFinish: " + response);
                    return;
                }
                if (body.code == ResponseBody.SUCCEED) {
                    callback.onSucceed(body.result);
                    mUserName = name;
                } else {
                    callback.onFailed("用户名或密码错误");
                }
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailed("连接失败");
                LogUtil.d("onFailure: " + e.toString());
            }
        });

    }

    public void getOrderedList() {
        if (mUserName.length() == 0) return;

        String get_order_url = ServerURL.getOrderURL(mUserName);
        HttpUtil.sendGETRequest(get_order_url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("message");

                    List<String> orderedList = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        LogUtil.d(array.getString(i));
                        orderedList.add(array.getString(i));
                    }

                    EventBus.getInstance().post(orderedList, new EventTag(EventTag.ORDERED_LIST));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void getRecommendedList() {
        if (mUserName.length() == 0) return;

        String get_recommended_url = ServerURL.getRecommendedListURL(mUserName);
        HttpUtil.sendGETRequest(get_recommended_url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("message");

                    List<String> recommendedList = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        LogUtil.d(array.getString(i));
                        recommendedList.add(array.getString(i));
                    }

                    EventBus.getInstance().post(recommendedList, new EventTag(EventTag.RECOMMENDED_LIST));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public String getUserName() {
        return mUserName;
    }
}
