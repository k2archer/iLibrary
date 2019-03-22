package com.kwei.ilibrary.comm.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.kwei.ilibrary.util.LogUtil;
import com.kwei.ilibrary.util.MD5Util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader {

    private Context mContext;
    private ImageCache mMemoryCache;
    private ImageCache mDiskCache;
    private ImageCache mNetworkCache;
    private LinkedHashMap<String, Boolean> UrlMD5List;
    private ExecutorService mExecutorService;

    public ImageLoader(Context context) {
        mContext = context;
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(context);
        mNetworkCache = new NetworkCache();
        UrlMD5List = new LinkedHashMap<>();
        mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void setDiskCache(ImageCache diskCache) {
        mDiskCache = diskCache;
    }

    public void setNetworkCache(ImageCache networkCache) {
        mNetworkCache = networkCache;
    }

    public void bindImage(final String url, final ImageView imageView) {
        String key = MD5Util.getMd5(url);
        if (imageView.getTag() != null && imageView.getTag().equals(key)) {
            Bitmap bitmap = null;
            try {
                bitmap = mMemoryCache.get(key);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                return;
            }
        }

        imageView.setTag(key);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                // 互斥，避免多线程同时从磁盘或者加载同一个 URL 的 Bitmap
                synchronized (UrlMD5List) {
                    if (UrlMD5List.get(url) != null) {
                        return;
                    } else {
                        UrlMD5List.put(url, true);
                    }
                }
                try {
                    Bitmap bitmap = loadBitmap(url);
                    if (bitmap != null) {
                        LoaderResult loaderResult = new LoaderResult(imageView, bitmap, url);
                        mHandler.obtainMessage(1, loaderResult).sendToTarget();
                    }
                    synchronized (UrlMD5List) {
                        UrlMD5List.remove(url);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mExecutorService.execute(task);
    }

    private Bitmap loadBitmap(String url) throws IOException {
        String key = MD5Util.getMd5(url);
        Bitmap bitmap = null;
        if (mDiskCache != null) {
            bitmap = mDiskCache.get(key);
            if (bitmap != null) {
                LogUtil.d("loadBitmapFromDiskCache: , url " + url);
                mMemoryCache.put(key, bitmap);
                return bitmap;
            }
        }

        if (mNetworkCache != null) {
            bitmap = mNetworkCache.get(url);
            if (bitmap != null) {
                LogUtil.d("loadBitmapFromNetworkCache: url " + url);
                mDiskCache.put(key, bitmap);
                return bitmap;
            }
        }

        return bitmap;
    }

    private class LoaderResult {
        private LoaderResult(ImageView imageView, Bitmap bitmap, String url) {
            this.imageView = imageView;
            this.bitmap = bitmap;
            this.url = url;
        }

        private ImageView imageView;
        private Bitmap bitmap;
        private String url;
    }

    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LoaderResult loader = (LoaderResult) msg.obj;
                    ImageView imageView = loader.imageView;
                    imageView.setImageBitmap(loader.bitmap);
                    break;
            }
        }
    };

}
