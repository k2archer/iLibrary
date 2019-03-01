package com.kwei.ilibrary.util;

public interface HttpCallbackListener {
    void onFinish(String response);

    void onFailure(Exception e);
}
