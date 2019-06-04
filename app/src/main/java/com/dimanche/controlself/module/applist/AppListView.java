package com.dimanche.controlself.module.applist;

import com.dimanche.controlself.data.entity.AppInfo;

import java.util.List;

public interface AppListView {

    /**
     * 获取已安装的应用列表
     *
     * @param list
     */
    void installedApp(List<AppInfo> list);
}
