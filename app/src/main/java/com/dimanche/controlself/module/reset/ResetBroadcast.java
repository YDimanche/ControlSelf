package com.dimanche.controlself.module.reset;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.dimanche.controlself.R;
import com.dimanche.controlself.data.db.DBHelper;
import com.dimanche.controlself.data.entity.AppInfo;
import com.dimanche.controlself.utils.ConversionUtils;
import com.dimanche.controlself.utils.TimeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ResetBroadcast extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {

        ConversionUtils.resetApp(context);


    }
}
