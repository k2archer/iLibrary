package com.kwei.ilibrary.comm.HttpManager;

public interface HttpCallBack {
    void onSucceed(HttpResponse object);

    void onFailure(HttpResponse object, Exception e);
}
