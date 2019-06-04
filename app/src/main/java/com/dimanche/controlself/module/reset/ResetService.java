package com.dimanche.controlself.module.reset;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.dimanche.controlself.utils.ConversionUtils;

import androidx.annotation.Nullable;

/**
 * 设置定时任务，每天零点将数据归零
 * Dimanche
 */
public class ResetService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long intervalTime = 60 * 60 * 24 * 1000;//间隔时间
        Intent timeIntent = new Intent(this, ResetBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, timeIntent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), intervalTime, pendingIntent);
        //重置应用使用时间
        ConversionUtils.resetApp(this);
        return super.onStartCommand(intent, flags, startId);
    }
}
