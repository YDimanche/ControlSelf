package com.dimanche.controlself.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.dimanche.controlself.data.db.DBHelper;
import com.dimanche.controlself.data.entity.AppInfo;
import com.dimanche.controlself.data.entity.TimeEntity;
import com.dimanche.controlself.module.monitor.MonitorService;
import com.dimanche.controlself.module.monitor.ServiceReceiver;
import com.dimanche.controlself.module.reset.ResetService;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
/**
 * Created by dimanche on 2019/5/15.
 */

public class AndroidApplication extends LitePalApplication {

    private static Context mContext;
    private static ArrayList<Activity> activityList;
    public static List<AppInfo> monitorList;
    public static HashSet<String> monitorSet;
    public static HashMap<String, AppInfo> monitorMap;
    private ServiceReceiver mServiceReceiver;
    public static boolean isThread = true;//是否开启监听循环，当灭屏的时候停止循环

    @Override
    public void onCreate() {
        super.onCreate();
        //注册广播
        mServiceReceiver = new ServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mServiceReceiver, filter);
        mContext = this;
        activityList = new ArrayList<>();
        monitorList = new ArrayList<>();
        monitorSet = new HashSet<>();
        monitorMap = new HashMap<>();
        startService(new Intent(this, MonitorService.class));
        startService(new Intent(this, ResetService.class));
        getMonitorApp();
    }

    public static Context getmContext() {
        return mContext;
    }

    public static void setActivity(Activity activity) {
        if (null != activityList) {
            activityList.add(activity);
        }
    }

    public static void remove(Activity activity) {
        if (null != activityList && activityList.size() > 0) {
            activityList.remove(activity);
        }
    }

    public static void finishApp() {
        if (null != activityList && activityList.size() > 0) {

            for (Activity activity :
                    activityList
            ) {
                activity.finish();
            }
        }
    }

    /**
     * 获取需要监控的app列表
     */
    public static void getMonitorApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                monitorMap.clear();
                monitorList.clear();
                monitorSet.clear();
                List<AppInfo> list = DBHelper.selectMonitor();
                Log.e("被监控的数量", list == null ? "0" : monitorList.size() + "");
                for (AppInfo appInfo : list) {
                    monitorSet.add(appInfo.getPackgeName());
                    List<TimeEntity> timeList = DBHelper.selectTimeSetList(appInfo.getId());
                    appInfo.setList(timeList);
                    monitorList.add(appInfo);
                    monitorMap.put(appInfo.getPackgeName(), appInfo);
                }
            }
        }).start();
    }
}
