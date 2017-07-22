package com.lst.lscourier.utils;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by lst718-011 on 2017/6/15.
 */
public class ToastUtils {

    public static void showToast(Activity activity ,String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
