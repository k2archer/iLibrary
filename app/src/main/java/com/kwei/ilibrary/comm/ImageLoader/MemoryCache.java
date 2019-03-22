package com.kwei.ilibrary.comm.ImageLoader;

import android.graphics.Bitmap;
import android.util.LruCache;

public class MemoryCache implements ImageCache {
    private LruCache<String, Bitmap> mMemLruCache;

    public MemoryCache() {
        initImageLoader();
    }

    private void initImageLoader() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 4;
        mMemLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }


    @Override
    public void put(String key, Bitmap bitmap) {
        mMemLruCache.put(key, bitmap);
    }

    @Override
    public Bitmap get(String key) {
        return mMemLruCache.get(key);
    }

    public void clear() {
        mMemLruCache.evictAll();
    }

}
