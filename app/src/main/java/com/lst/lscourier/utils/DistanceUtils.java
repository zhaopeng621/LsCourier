package com.lst.lscourier.utils;

import android.content.Context;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;

import java.math.BigDecimal;

/**
 * 验证手机
 */
public class DistanceUtils {
    private static LatLng startLatlng, endLatlng;

    //计算距离
    public static float calculateFrom(Context activity, String EndLatitude, String EndLongitude) {
        startLatlng = new LatLng(Double.valueOf(SharePrefUtil.getString(activity, "MyLatitude", "")),
                Double.valueOf(SharePrefUtil.getString(activity, "MyLongitude", "")));
        endLatlng = new LatLng(Double.valueOf(EndLatitude), Double.valueOf(EndLongitude));
        float m = AMapUtils.calculateLineDistance(startLatlng, endLatlng);
        float km = new BigDecimal(m / 1000).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return km;
    }

}
