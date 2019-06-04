package com.dimanche.controlself.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dimanche on 2019/5/15
 * app实体类
 */
public class AppInfo extends DataSupport {

    private int id;
    private String appName;//应用名
    private String packgeName;//包名
    private String isMonitor;//是否被监控00是01否
    private String isShowWindow;//是否显示悬浮窗00是01否
    private String type;//监控类型，00是按照时间总量01是按照时间段
    private int totalTime;//时间总量 单位分钟
    private int surplus;//剩余的时间 单位毫秒
    private List<TimeEntity> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackgeName() {
        return packgeName;
    }

    public void setPackgeName(String packgeName) {
        this.packgeName = packgeName;
    }

    public String getIsMonitor() {
        return isMonitor;
    }

    public void setIsMonitor(String isMonitor) {
        this.isMonitor = isMonitor;
    }

    public String getIsShowWindow() {
        return isShowWindow;
    }

    public void setIsShowWindow(String isShowWindow) {
        this.isShowWindow = isShowWindow;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getSurplus() {
        return surplus;
    }

    public void setSurplus(int surplus) {
        this.surplus = surplus;
    }

    public List<TimeEntity> getList() {
        return list;
    }

    public void setList(List<TimeEntity> list) {
        this.list = list;
    }
}
