package com.kwei.ilibrary.comm.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kwei.ilibrary.comm.HttpManager.HttpClientManager;
import com.kwei.ilibrary.comm.HttpManager.HttpResponse;
import com.kwei.ilibrary.util.LogUtil;

import java.io.IOException;

public class NetworkCache implements ImageCache {

    @Override
    public void put(String key, Bitmap bitmap) {
        LogUtil.d("NetworkCache is no support put().");
    }

    @Override
    public Bitmap get(String url) throws IOException {
        HttpResponse response = HttpClientManager.get(url);
        return BitmapFactory.decodeByteArray(response.body, 0, response.body.length);
    }
}
