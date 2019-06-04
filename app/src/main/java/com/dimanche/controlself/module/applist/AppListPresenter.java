package com.dimanche.controlself.module.applist;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.dimanche.controlself.base.BasePresenter;
import com.dimanche.controlself.data.appdata.AppData;
import com.dimanche.controlself.data.db.DBHelper;
import com.dimanche.controlself.data.entity.AppInfo;
import com.dimanche.controlself.utils.ConversionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AppListPresenter extends BasePresenter {
    AppListView view;

    public void attachView(AppListView view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    public boolean isViewAttached() {
        return view != null;
    }

    /**
     * 获取已经安装的app
     */
    public void getInstalled(final PackageManager mPackgeManager) {
        Observable.create(new ObservableOnSubscribe<List<AppInfo>>() {

            @Override
            public void subscribe(ObservableEmitter<List<AppInfo>> e) {
                try {

                    //获取已安装的app
                    List<ResolveInfo> resolveInfos = AppData.getInstalled(mPackgeManager);
                    List<AppInfo> appList = ConversionUtils.transfor(resolveInfos, mPackgeManager);
                    HashMap<String, AppInfo> hashMapApp = new HashMap<>();
                    for (AppInfo appInfo : appList) {
                        hashMapApp.put(appInfo.getPackgeName(), appInfo);
                    }

                    //获取数据库中记录的app
                    List<AppInfo> dbList = DBHelper.selectAppDatas();
                    HashMap<String, AppInfo> hashMapDB = new HashMap<>();
                    for (AppInfo appInfo : dbList) {
                        hashMapDB.put(appInfo.getPackgeName(), appInfo);
                    }

                    //如果有新安装的软件
                    List<AppInfo> listNew = new ArrayList<>();
                    for (AppInfo appInfo : appList) {
                        if (!hashMapDB.containsKey(appInfo.getPackgeName())) {
                            listNew.add(appInfo);
                        }
                    }
                    DBHelper.saveAppInfo(listNew);

                    //如果有软件卸载
                    List<AppInfo> listUnIns = new ArrayList<>();
                    for (AppInfo appInfo : dbList) {
                        if (!hashMapApp.containsKey(appInfo.getPackgeName())) {
                            listUnIns.add(appInfo);
                        }
                    }
                    DBHelper.deleteApp(listUnIns);
                    dbList = DBHelper.selectAppDatas();
                    e.onNext(dbList);
                    e.onComplete();

                } catch (Exception e1) {
                    e.tryOnError(e1);
                    e.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<AppInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<AppInfo> resolveInfos) {
                if (isViewAttached()) {
                    view.installedApp(resolveInfos);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 更新锁的状态
     *
     * @param list
     */
    public void updateStatus(List<AppInfo> list) {
        for (AppInfo appInfo : list) {
            DBHelper.updateMonitor(appInfo);
        }
    }

}
