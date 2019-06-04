package com.dimanche.controlself.module.appset;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dimanche.controlself.R;
import com.dimanche.controlself.base.AndroidApplication;
import com.dimanche.controlself.base.BaseActivity;
import com.dimanche.controlself.data.entity.AppInfo;
import com.dimanche.controlself.data.entity.TimeEntity;
import com.dimanche.controlself.module.appset.fragment.TimeAlotFragment;
import com.dimanche.controlself.module.appset.fragment.TimeTotalFragment;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AppSetActivity extends BaseActivity implements AppSetView {
    //标题头
    ImageView bac;
    TextView title;

    //时间段和时间总量的单选框
    RadioButton timeAlot, timeTotal;

    //时间段和时间总量的fragment
    TimeAlotFragment timeAlotFragment;
    TimeTotalFragment timeTotalFragment;

    //是否进行监控和开启悬浮窗
    CheckBox monitoringCheck, windowCheck;
    //其他
    AppSetPresenter presenter;
    AppInfo appInfo;
    public static List<TimeEntity> timeList;//用于存放时间的集合

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_set);
        init();
        click();
        setTitle();
    }

    /**
     * 初始化对象及控件
     */
    private void init() {
        presenter = new AppSetPresenter();
        presenter.attachView(this);

        timeAlot = findViewById(R.id.time_alot);
        timeTotal = findViewById(R.id.time_total);
        monitoringCheck = findViewById(R.id.monitoring_check);
        windowCheck = findViewById(R.id.window_check);
        //加载APPinfo数据
        Intent intent = getIntent();
        presenter.selectAppInfon(intent.getIntExtra("id", 0));
        //初始化fragment
        timeTotalFragment = new TimeTotalFragment();
        timeAlotFragment = new TimeAlotFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fram, timeTotalFragment).commit();
    }


    private void click() {
        //时间段
        timeAlot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram, timeAlotFragment).commit();
                }
            }
        });
        //时间总量
        timeTotal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fram, timeTotalFragment).commit();
                }
            }
        });
        //保存
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAppInfo();
            }
        });
    }

    /**
     * 设置标题头
     */
    private void setTitle() {
        title = findViewById(R.id.title);
        bac = findViewById(R.id.bac);

        //返回
        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 保存应用信息
     */
    private void saveAppInfo(){
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fram);

        appInfo.setIsMonitor(monitoringCheck.isChecked() == true ? "00" : "01");
        appInfo.setIsShowWindow(windowCheck.isChecked() == true ? "00" : "01");
        if (monitoringCheck.isChecked()) {
            if (current != null && current == timeTotalFragment) {
                appInfo.setType("00");
                int total = timeTotalFragment.getTotal();
                if (total <= 1) {
                    toast("总时间不能小于2分钟");
                    return;
                }
                appInfo.setTotalTime(total);
                appInfo.setSurplus(total * 60 * 1000);
            } else if (current != null && current == timeAlotFragment) {
                appInfo.setType("01");
                List<TimeEntity> list = timeAlotFragment.getList();
                if (null == list || list.size() == 0) {
                    toast("不能没有时间段哦<^_^>");
                    return;
                }
                appInfo.setList(list);
            }
        }
        presenter.saveSet(appInfo);

    }


    /**
     * 初始化应用相关信息
     *
     * @param appInfo
     */
    @Override
    public void selectAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
        title.setText(appInfo.getAppName());
        monitoringCheck.setChecked("00".equals(appInfo.getIsMonitor()) ? true : false);
        windowCheck.setChecked("00".equals(appInfo.getIsShowWindow()) ? true : false);
        if ("00".equals(appInfo.getType())) {
            timeTotal.setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fram, timeTotalFragment).commit();
            timeTotalFragment.setTotal(appInfo.getTotalTime() + "");
        } else {
            timeAlot.setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fram, timeAlotFragment).commit();
            timeList = appInfo.getList();
        }
    }

    @Override
    public void saveSuccess() {
        toast("保存成功");
        finish();
    }
}
