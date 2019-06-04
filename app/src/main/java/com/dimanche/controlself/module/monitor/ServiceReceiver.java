package com.dimanche.controlself.module.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dimanche.controlself.base.AndroidApplication;

public class ServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        //Log.e("action", action);

        switch (action) {
            case Intent.ACTION_SCREEN_ON:  //屏幕开启的广播

                break;
            case Intent.ACTION_SCREEN_OFF: //屏幕关闭的广播
                AndroidApplication.isThread = false;
                Log.e("屏幕关闭", "屏幕关闭");
                break;
            case Intent.ACTION_USER_PRESENT:
                AndroidApplication.isThread = true;
                context.startService(new Intent(context, MonitorService.class));

                break;
        }
    }

}
