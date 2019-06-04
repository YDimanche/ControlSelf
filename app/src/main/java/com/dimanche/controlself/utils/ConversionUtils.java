package com.dimanche.controlself.utils;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.util.Log;

import com.dimanche.controlself.data.db.DBHelper;
import com.dimanche.controlself.data.entity.AppInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 应用相关工具类
 * Dimanche
 */
public class ConversionUtils {


    public static List<AppInfo> transfor(List<ResolveInfo> resolveInfos, PackageManager mPackgeManager) throws PackageManager.NameNotFoundException {
        List<AppInfo> list = new ArrayList<>();
        HashMap<String, AppInfo> hashMap = new HashMap<>();
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pacgeName = resolveInfo.activityInfo.packageName;
            ApplicationInfo applicationInfo = mPackgeManager.getApplicationInfo(pacgeName, PackageManager.GET_UNINSTALLED_PACKAGES);
            String appName = mPackgeManager.getApplicationLabel(applicationInfo).toString();
            //过滤掉系统设置中的应用
            if (!"com.android.settings".equals(pacgeName)) {
                //过滤掉重复数据
                if (!hashMap.containsKey(pacgeName)) {
                    AppInfo appInfo = new AppInfo();
                    appInfo.setAppName(appName);
                    appInfo.setPackgeName(pacgeName);
                    list.add(appInfo);
                    hashMap.put(pacgeName, appInfo);
                }
            }
        }
        return list;
    }

    /**
     * 获取栈顶应用包名
     */
    public static String getLauncherTopApp(Context context, ActivityManager activityManager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } else {
            //5.0以后需要用这方法
            UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                }
            }
            if (!android.text.TextUtils.isEmpty(result)) {
                return result;
            }
        }
        return "";
    }

    /**
     * 重置应用使用时间
     *
     * @param context
     */
    public static void resetApp(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = context.getSharedPreferences("ControlSelf", Context.MODE_PRIVATE);

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    //取出上次更新的时间
                    String lastDateStr = sharedPreferences.getString("updateTime", "2019-5-27");
                    Date lastDate = df.parse(lastDateStr);
                    //获取当前系统时间
                    String nowDateStr = TimeUtils.getNowDate();
                    Date nowDate = df.parse(nowDateStr);
                    if (nowDate.getTime() > lastDate.getTime()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("updateTime", nowDateStr);
                        editor.commit();
                        List<AppInfo> list = DBHelper.selectMonitor();
                        for (AppInfo appInfo : list) {
                            appInfo.setSurplus(appInfo.getTotalTime() * 60 * 1000);
                            DBHelper.saveAppInfo(appInfo);
                        }
                        Log.e("ResetBroadcast", "应用时间以重置");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
