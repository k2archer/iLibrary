package com.kwei.ilibrary.comm.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;
import com.kwei.ilibrary.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DiskCache implements ImageCache {
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50; // 50MB
    private DiskLruCache mDiskLruCache;
//    private boolean mIsDiskLruCacheCreated = false;


    public DiskCache(Context context) {
        File diskCacheDir = getDiskCacheDir(context, "bitmapCache");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdir();
        }
        // Todo 建议添加检查可用空间是否足够的代码
        try {
            // the second parameter is the version code
            // the third parameter is the number of nodes that are cached
            mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
//            mIsDiskLruCacheCreated = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        if (mDiskLruCache == null && bitmap == null) return;

        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(key);
            if (editor != null) {
                OutputStream out = editor.newOutputStream(0);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                out.write(byteArrayOutputStream.toByteArray());
                editor.commit();
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            try {
                if (editor != null) editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }

    }

    @Override
    public Bitmap get(String key) throws IOException {
        Bitmap bitmap = null;
        DiskLruCache.Snapshot snapshot = null;
        snapshot = mDiskLruCache.get(key);
        try {
            if (snapshot != null) {
                FileInputStream fileInStream = (FileInputStream) snapshot.getInputStream(0);
                FileDescriptor fd = fileInStream.getFD();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
                bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
                options.inSampleSize = computeSampleSize(options, 600, 600);
//                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                /* 下面两个字段需要组合使用 */
                options.inPurgeable = true;
                options.inInputShareable = true;
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeFileDescriptor(fd, null, options);
                LogUtil.d("loadBitmapFromDiskCache: bitmap size " + bitmap.getRowBytes() * bitmap.getHeight());
            }
        } catch (OutOfMemoryError e) {
            LogUtil.d("OutOfMemoryError");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueName);
    }

    private static int computeSampleSize(BitmapFactory.Options options, int reqWidth,
                                         int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }
}
