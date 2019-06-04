package com.dimanche.controlself.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 用于全局持久化存储变量，解决Android中的application存储数据不安全的问题
 *
 * @author Dimanche
 */

public class SharepreferenceApp {

    Context context;
    SharedPreferences sharedPreferences;
    Editor editor;
    String name = "";

    public SharepreferenceApp(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("controlself",
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 全局持久存储
     *
     * @param key   键
     * @param value 值
     */
    public void setSharepreference(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 返回指定键的值
     *
     * @param key arg0 用于备用，当对应的键值中没有数据则返回的值
     * @return
     */
    public String getSharepreference(String key, String arg0) {

        return sharedPreferences.getString(key, arg0);
    }

    /**
     * 全局持久存储
     *
     * @param key   键
     * @param value 值
     */
    public void setSharepreference(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 返回指定键的值
     *
     * @param key arg0 用于备用，当对应的键值中没有数据则返回的值
     * @return
     */
    public Boolean getSharepreference(String key, Boolean arg0) {

        return sharedPreferences.getBoolean(key, arg0);
    }

}
