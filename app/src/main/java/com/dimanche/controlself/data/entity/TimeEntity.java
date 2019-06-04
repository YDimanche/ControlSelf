package com.dimanche.controlself.data.entity;

import org.litepal.crud.DataSupport;

public class TimeEntity extends DataSupport {
    private int id;
    private String startTime;
    private String endTime;
    private int appInfo_id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getAppInfo_id() {
        return appInfo_id;
    }

    public void setAppInfo_id(int appInfo_id) {
        this.appInfo_id = appInfo_id;
    }
}
