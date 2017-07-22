package com.lst.lscourier.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by lst718-011 on 2017/7/22.
 */
public class TimeUtils {
    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分"）
     */
    public static String timet(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }
}
