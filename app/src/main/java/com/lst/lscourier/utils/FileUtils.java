

package com.lst.lscourier.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * 文件名	：文件操作类
 * 创建日期	：2012-10-15
 * Copyright (c) 2003-2012 北京联龙博通
 */
public class FileUtils {
    private static String Header_pic_name = "header.jpg";

    private static String HEADPATH = Environment.getExternalStorageDirectory()
            + "/formats/";

    /**
     * 检测授权
     *
     * @param context
     * @param ManifestPermission
     * @return true:已经授权   false没有授权
     */
    public static boolean insertDummyContactWrapper(Activity context, String ManifestPermission) {
        int hasWriteContactsPermission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasWriteContactsPermission = context.checkSelfPermission(ManifestPermission);
        }
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(new String[]{ManifestPermission},
                        1);
            }
            return false;
        } else {
            return true;
        }
    }

    public static File makeFile() {
        File tempFile = new File(HEADPATH, Header_pic_name); // 以时间秒为文件名
        File temp = new File(FileUtils.HEADPATH);//自已项目 文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
        return tempFile;
    }
}
