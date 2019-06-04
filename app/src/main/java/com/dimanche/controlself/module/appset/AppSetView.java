package com.dimanche.controlself.module.appset;

import com.dimanche.controlself.data.entity.AppInfo;

public interface AppSetView {

    void selectAppInfo(AppInfo appInfo);

    /**
     * 保存成功
     */
    void saveSuccess();
}
