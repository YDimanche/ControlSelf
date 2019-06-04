package com.dimanche.controlself.module.monitor;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.dimanche.controlself.base.AndroidApplication;
import com.dimanche.controlself.data.db.DBHelper;
import com.dimanche.controlself.data.entity.AppInfo;
import com.dimanche.controlself.data.entity.TimeEntity;
import com.dimanche.controlself.module.rest.RestActivity;
import com.dimanche.controlself.utils.ConversionUtils;
import com.dimanche.controlself.utils.TimeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;

public class MonitorService extends IntentService {
    private ActivityManager activityManager;
    private int sleep = 1000;//监听的间隔时间
    private String lastPackgeName = "";
    DateFormat df;
    private final String TAG = "MonitorService";



    public MonitorService() {
        super("MonitorService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            checkApp();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("我是服务", "我被创建了");
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        df = new SimpleDateFormat("HH:mm");


    }

    /**
     * 监控当前是否打开了目标app
     */
    private void checkApp() throws InterruptedException {
        while (AndroidApplication.isThread) {
            Thread.sleep(sleep);

            //获取当前打开的app包名
            String packgeApp = ConversionUtils.getLauncherTopApp(this, activityManager);
//            Log.e(TAG,packgeApp);
            if ("".equals(packgeApp)) {
                packgeApp = lastPackgeName;
            }
            //如果本次检查到的包名和上次检查到的包名不相同
            if (!lastPackgeName.equals(packgeApp)) {
                //如果上次的包名是被监控的话，则将上次的应用数据同步到数据库
                if (AndroidApplication.monitorSet.contains(lastPackgeName)) {
                    AppInfo appInfo = AndroidApplication.monitorMap.get(lastPackgeName);
                    DBHelper.saveAppInfo(appInfo);
                    Log.e("MonitorService", "数据已同步到数据库");
                }
                lastPackgeName = packgeApp;
            }

            //判断当前应用是否被监控
            if (AndroidApplication.monitorSet.contains(packgeApp)) {
                AppInfo appInfo = AndroidApplication.monitorMap.get(packgeApp);
                //按照时间总量监控
                if ("00".equals(appInfo.getType())) {
                    totalTime(appInfo, packgeApp);
                } else {//按照时间段进行监控
                    timeSlot(appInfo);
                }
            }
        }
    }

    /**
     * 按照时间总量进行监控
     *
     * @param appInfo
     * @param packgeApp
     */
    private void totalTime(AppInfo appInfo, String packgeApp) {
        //获取剩余时间
        if (appInfo.getSurplus() <= 0) {
            intentRest();
        } else {
            AndroidApplication.monitorMap.get(packgeApp).setSurplus(appInfo.getSurplus() - sleep);
        }
    }

    /**
     * 按照时间段进行监控
     */
    private void timeSlot(AppInfo appInfo) {

        //获取当前时间
        String nowTime = TimeUtils.getNowTime();
        //判断当前时间是否在不可使用的时间段内
        List<TimeEntity> timeEntityList = appInfo.getList();
        for (TimeEntity entity : timeEntityList) {
            String startTime = entity.getStartTime();
            String endTime = entity.getEndTime();
            try {
                Date startDate = df.parse(startTime);
                Date endDate = df.parse(endTime);
                Date nowDate = df.parse(nowTime);
                if (nowDate.getTime() > startDate.getTime() && nowDate.getTime() < endDate.getTime()) {
                    Log.e(TAG, appInfo.getAppName());
                    intentRest();
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 跳转到休息界面
     */
    private void intentRest() {
        Message mm = handler.obtainMessage();
        mm.what = 0x11;
        handler.sendMessage(mm);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x11:
                    Intent dialogIntent = new Intent(MonitorService.this, RestActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
