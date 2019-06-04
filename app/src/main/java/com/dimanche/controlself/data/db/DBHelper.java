package com.dimanche.controlself.data.db;

import android.content.ContentValues;

import com.dimanche.controlself.data.entity.AppInfo;
import com.dimanche.controlself.data.entity.TimeEntity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimanche on 2019/5/15
 * 数据库使用帮助类
 */
public class DBHelper {

    /**
     * 保存app列表信息
     *
     * @param list
     */
    public static void saveAppInfo(List<AppInfo> list) {
        DataSupport.saveAll(list);
    }

    /**
     * 保存设置的主表信息
     */
    public static int updateMonitor(AppInfo appInfo) {
        return appInfo.update(appInfo.getId());
    }

    /**
     * 保存app设置的时间段信息
     *
     * @param entity
     */
    public static TimeEntity sageAppTimeSet(TimeEntity entity) {
        entity.save();
        return entity;
    }

    /**
     * 保存应用对象信息
     * @param appInfo
     * @return
     */
    public static boolean saveAppInfo(AppInfo appInfo){

        return appInfo.save();
    }

    /**
     * 获取需要监控的
     * @return
     */
    public static List<AppInfo>selectMonitor(){
        List<AppInfo>list=DataSupport.where("isMonitor=?","00").find(AppInfo.class);
        return list;
    }

    /**
     * 根据APPinfoid获取时间段
     * @param id
     * @return
     */
    public static List<TimeEntity> selectTimeSetList(int id) {
        List<TimeEntity> list = DataSupport.where("appinfo_id=?", id + "").find(TimeEntity.class);
        return list;
    }



    /**
     * 获取app列表
     *
     * @return
     */
    public static List<AppInfo> selectAppDatas() {
        List<AppInfo> list = DataSupport.findAll(AppInfo.class, true);
        return list;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public static AppInfo selectAppInfo(int id) {
        AppInfo appInfo = DataSupport.find(AppInfo.class, id);
        return appInfo;
    }


    /**
     * 删除相关数据
     */
    public static void deleteApp(List<AppInfo> list) {
        for (AppInfo appInfo : list) {
            DataSupport.delete(AppInfo.class, appInfo.getId());
        }
    }

    /**
     * 根据应用id删除应用监控时间段数据
     */
    public static void deleteTime(int appinfoId){
        DataSupport.deleteAll(TimeEntity.class,"appInfo_id=?",appinfoId+"");
    }


}
