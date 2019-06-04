package com.dimanche.controlself.utils;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

/**
 * Dimanche
 * 权限帮助类
 */
public class PermissionUtils {

    /**
     *判断是否有权查看其他应用使用信息的权限
     * @param mContext
     * @return
     */
    public static boolean checkOtherAppUse(Context mContext) {
        AppOpsManager opsManager = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
        int mode = opsManager.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), mContext.getPackageName());
        return  mode == AppOpsManager.MODE_ALLOWED;
    }

    /**
     * 判断是否打开悬浮窗
     * @return
     */
    public static boolean canshowWindow(Context mContext){
        AppOpsManager opsManager = (AppOpsManager) mContext.getSystemService(Context.APP_OPS_SERVICE);
        int mode = opsManager.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), mContext.getPackageName());
        return  mode == AppOpsManager.MODE_ALLOWED;
    }
}
