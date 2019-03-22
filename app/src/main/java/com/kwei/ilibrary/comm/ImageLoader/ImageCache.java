package com.kwei.ilibrary.comm.ImageLoader;

import android.graphics.Bitmap;

import java.io.IOException;

public interface ImageCache {
    void put(String key, Bitmap bitmap);

    Bitmap get(String key) throws IOException;
}
