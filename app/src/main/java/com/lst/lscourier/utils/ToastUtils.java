package com.lst.lscourier.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lst718-011 on 2017/6/15.
 */
public class ToastUtils {

    public static void showToast(Context activity , String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
