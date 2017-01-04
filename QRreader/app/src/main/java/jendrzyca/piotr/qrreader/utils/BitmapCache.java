package jendrzyca.piotr.qrreader.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.LruCache;

/**
 * Created by Piotr Jendrzyca on 11.12.2016.
 *
 * Class to store bitmaps and to access throughout app
 */

public class BitmapCache {
    private LruCache<String, Bitmap> cache;

    public BitmapCache(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        cache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public void addBitmap(String key, Bitmap value) {
        cache.put(key, rotate(value));
    }

    public Bitmap getBitmap(String key) {
        return cache.get(key);
    }

    private Bitmap rotate(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        matrix.postRotate(180);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }
}
