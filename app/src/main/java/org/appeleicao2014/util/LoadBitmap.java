package org.appeleicao2014.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import org.appeleicao2014.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import static org.appeleicao2014.util.ImageCommons.decodeSampledBitmapFromResource;

/**
 * Created by thaleslima on 8/24/14.
 */
public class LoadBitmap {
    private LruCache<String, Bitmap> mMemoryCache;
    private Context context;
    private Bitmap mPlaceHolderBitmap;

    public LoadBitmap(Context context)
    {
        mPlaceHolderBitmap = decodeSampledBitmapFromResource(context.getResources(), R.drawable.photo, 110, 110);

        this.context = context;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB_MR1) {
                    return bitmap.getByteCount() / 1024;
                } else {
                    return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                }
            }
        };
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void loadBitmap(String imageUrl, ImageView imageView) {
        final Bitmap bitmap = getBitmapFromMemCache(imageUrl);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            if (cancelPotentialWork(imageUrl, imageView)) {
                final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
                final AsyncDrawable asyncDrawable =
                        new AsyncDrawable(context.getResources(), mPlaceHolderBitmap, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(imageUrl);
            }
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        //        private int resId = 0;
        private String imageUrl;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            ImageView thumbnail = imageViewReference.get();
            try {
                InputStream is = (InputStream) new URL(imageUrl).getContent();
                Bitmap bitmap = ImageCommons.decodeSampledBitmapFromInputStream(is, thumbnail.getWidth(), thumbnail.getHeight());
                addBitmapToMemoryCache(imageUrl, bitmap);
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask =
                        getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask && imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public static boolean cancelPotentialWork(String imageUrl, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null && bitmapWorkerTask.imageUrl != null) {
            final String bitmapWorkerTaskImageUrl = bitmapWorkerTask.imageUrl;
            if (!bitmapWorkerTaskImageUrl.equals(imageUrl)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }
}
