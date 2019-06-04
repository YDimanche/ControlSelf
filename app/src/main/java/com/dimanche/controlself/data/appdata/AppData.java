package com.dimanche.controlself.data.appdata;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.dimanche.controlself.data.db.DBHelper;
import com.dimanche.controlself.data.entity.AppInfo;
import com.dimanche.controlself.utils.ConversionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimanche on 2019/5/15
 */
public class AppData {
    /**
     * 获取当前安装了哪些应用
     */
    public static List<ResolveInfo> getInstalled(PackageManager mPackgeManager) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = mPackgeManager.queryIntentActivities(intent, 0);
        return resolveInfos;
    }



}
