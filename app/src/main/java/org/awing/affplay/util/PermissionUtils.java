package org.awing.affplay.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import static android.os.Build.VERSION_CODES.P;

public class PermissionUtils {
    public static final int REQ_PERM_STORAGE = 1;
    /* * android 动态权限申请 * */
    public static void verifyStoragePermissions(Activity activity) {
        String[] storagePermissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, storagePermissions, REQ_PERM_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkRequestPermissions(Activity activity, String[] permissions, int reqCode) {
        try {
            boolean granted = true;
            for (int i = 0; i < permissions.length; i++) {
                int result = ActivityCompat.checkSelfPermission(activity, permissions[i]);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
            if (!granted) {
                ActivityCompat.requestPermissions(activity, permissions, reqCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isStoragePermissionsGranted(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQ_PERM_STORAGE) {
            if (grantResults.length >= 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }
}
