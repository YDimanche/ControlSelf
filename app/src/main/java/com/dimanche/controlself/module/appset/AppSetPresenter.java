package com.dimanche.controlself.module.appset;

import com.dimanche.controlself.base.AndroidApplication;
import com.dimanche.controlself.base.BasePresenter;
import com.dimanche.controlself.data.db.DBHelper;
import com.dimanche.controlself.data.entity.AppInfo;
import com.dimanche.controlself.data.entity.TimeEntity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class AppSetPresenter extends BasePresenter {

    AppSetView view;

    public void attachView(AppSetView view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    public boolean isViewAttachView() {
        return view != null;
    }

    /**
     * 根据id获取APPinfo
     *
     * @param id
     */
    public void selectAppInfon(final int id) {
        Observable.create(new ObservableOnSubscribe<AppInfo>() {
            @Override
            public void subscribe(ObservableEmitter<AppInfo> e) throws Exception {
                List<TimeEntity> timeEntityList = DBHelper.selectTimeSetList(id);
                AppInfo appInfo = DBHelper.selectAppInfo(id);
                appInfo.setList(timeEntityList);
                e.onNext(appInfo);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<AppInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AppInfo appInfo) {
                if (isViewAttachView()) {
                    view.selectAppInfo(appInfo);
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
     * 保存应用的设置信息
     *
     * @param appInfo
     */
    public void saveSet(final AppInfo appInfo) {

        Observable.create(new ObservableOnSubscribe<AppInfo>() {
            @Override
            public void subscribe(ObservableEmitter<AppInfo> e) throws Exception {
                //将数据插入到数据库中
                List<TimeEntity> timeList = appInfo.getList();
                DBHelper.deleteTime(appInfo.getId());
                for (TimeEntity entity : timeList) {
                    entity.setAppInfo_id(appInfo.getId());
                    DBHelper.sageAppTimeSet(entity);
                }
                int count = DBHelper.updateMonitor(appInfo);
                appInfo.update(appInfo.getId());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<AppInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AppInfo appInfo) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                if (isViewAttachView()) {
                    view.saveSuccess();
                    AndroidApplication.getMonitorApp();
                }
            }
        });

    }


}
