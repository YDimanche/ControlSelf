package com.dimanche.controlself.utils;



import java.util.Calendar;

public class TimeUtils {
    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String getNowTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour+":"+minute;
    }

    /**
     * 获取当前系统日期
     * @return
     */
    public static String getNowDate(){
        Calendar calendar = Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int mothon=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        return year+"-"+mothon+"-"+day;
    }


}
