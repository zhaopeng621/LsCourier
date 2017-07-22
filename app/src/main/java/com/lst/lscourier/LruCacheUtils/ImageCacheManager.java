package com.lst.lscourier.LruCacheUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.lst.lscourier.app.App;


/**
 * 图片缓存管理类 获取ImageLoader对象
 *
 * @author Rabbit_Lee
 */
public class ImageCacheManager {

    private static String TAG = ImageCacheManager.class.getSimpleName();

    // 获取图片缓存类对象
    private static ImageCache mImageCache = new ImageCacheUtil();
    // 获取ImageLoader对象
    public static ImageLoader mImageLoader = new ImageLoader(App.getHttpQueue(), mImageCache);

    /**
     * 获取ImageListener
     *
     * @param view
     * @param defaultImage
     * @param errorImage
     * @return
     */
    public static ImageListener getImageListener(final ImageView view, final Bitmap defaultImage, final Bitmap errorImage) {

        return new ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // 回调失败
                if (errorImage != null) {
                    view.setImageBitmap(errorImage);
                }
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                // 回调成功
                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                } else if (defaultImage != null) {
                    view.setImageBitmap(defaultImage);
                }
            }
        };

    }

    /**
     * 提供给外部调用方法
     *
     * @param url
     * @param view
     * @param defaultImage
     * @param errorImage
     */
    public static void loadImage(String url, ImageView view, Bitmap defaultImage, Bitmap errorImage) {
        mImageLoader.get(url, ImageCacheManager.getImageListener(view, defaultImage, errorImage), 0, 0);
    }

    /**
     * 提供给外部调用方法
     *
     * @param url
     * @param view
     * @param defaultImage
     * @param errorImage
     */
    public static void loadImage(String url, ImageView view, Bitmap defaultImage, Bitmap errorImage, int maxWidth, int maxHeight) {
        mImageLoader.get(url, ImageCacheManager.getImageListener(view, defaultImage, errorImage), maxWidth, maxHeight);
    }
    public static Bitmap getBitmapFromRes(Context context, int resId) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, resId);

    }
}