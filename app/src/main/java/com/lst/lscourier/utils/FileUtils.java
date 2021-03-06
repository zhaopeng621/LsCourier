

package com.lst.lscourier.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * 文件名	：文件操作类
 */
public class FileUtils {
    private static String Header_pic_name = "header_" + String.valueOf(System.currentTimeMillis())
            + ".jpg";
    private static String Header_pic_name1 = "header1_" + String.valueOf(System.currentTimeMillis())
            + ".jpg";
    private static String Header_pic_name2 = "header2_" + String.valueOf(System.currentTimeMillis())
            + ".jpg";

    private static String HEADPATH = Environment.getExternalStorageDirectory()
            + "/formats/";
    private static File temp = new File(FileUtils.HEADPATH);//自已项目 文件夹

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
        if (!temp.exists()) {
            temp.mkdir();
        }
        return tempFile;
    }

    public static File makeFile1() {
        File tempFile = new File(HEADPATH, Header_pic_name1); // 以时间秒为文件名
        if (!temp.exists()) {
            temp.mkdir();
        }
        return tempFile;
    }

    public static File makeFile2() {
        File tempFile = new File(HEADPATH, Header_pic_name2); // 以时间秒为文件名
        if (!temp.exists()) {
            temp.mkdir();
        }
        return tempFile;
    }
}
