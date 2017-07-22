package com.lst.lscourier.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.lst.lscourier.LruCacheUtils.ImageCacheManager;
import com.lst.lscourier.R;
import com.lst.lscourier.app.App;


/**
 * Created by lst718-011 on 2017/6/9.
 */
public class ImageViewHolder implements Holder<String> {
    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }
    @Override
    public void UpdateUI(Context context, int position, String data) {
        ImageCacheManager.loadImage(data, imageView, ImageCacheManager.getBitmapFromRes(App.getInstance(), R.mipmap.ic_launcher),
                ImageCacheManager.getBitmapFromRes(App.getInstance(), R.mipmap.ic_launcher));

//        imageView.setImageResource(data);
    }
}