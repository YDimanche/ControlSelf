package com.dimanche.controlself.module.applist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dimanche.controlself.R;
import com.dimanche.controlself.adapter.AppInfoAdapter;
import com.dimanche.controlself.base.BaseActivity;
import com.dimanche.controlself.data.entity.AppInfo;
import com.dimanche.controlself.module.appset.AppSetActivity;
import com.dimanche.controlself.utils.PermissionUtils;
import com.dimanche.controlself.utils.SharepreferenceApp;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AppListActivity extends BaseActivity implements AppListView {
    Context mContext;
    SharepreferenceApp sharepreferenceApp;
    AppListPresenter presenter;
    PackageManager packageManager;
    RecyclerView recyclerView;
    AppInfoAdapter adapter;
    List<AppInfo> list;
    LinearLayoutManager layoutManager;//用于设置recyclerview的样式（横向纵向瀑布流）


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        mContext = this;
        init();
        setTitle();
        checkPermission();
    }


    @SuppressLint("WrongConstant")
    private void init() {
        sharepreferenceApp = new SharepreferenceApp(mContext);
        packageManager = getPackageManager();
        presenter = new AppListPresenter();
        presenter.attachView(this);

        //recyclerview部分
        list = new ArrayList<>();
        adapter = new AppInfoAdapter(this, list);
        recyclerView = findViewById(R.id.rec);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);//纵向
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new AppInfoAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(AppListActivity.this, AppSetActivity.class);
                intent.putExtra("id", list.get(position).getId());
                startActivity(intent);
            }
        });

    }

    /**
     * 设置标题头
     */
    private void setTitle() {
        TextView title = findViewById(R.id.title);
        ImageView bac = findViewById(R.id.bac);
        title.setText("应用");
        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 获取权限
     */
    private void checkPermission() {

        if (!PermissionUtils.checkOtherAppUse(mContext)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("说明");
            builder.setMessage("我们需要获取查看其他应用使用情况的权限，如果您不赋予此权限则无法使用该软件,点击确定跳转至获取权限的页面，请打开该权限");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.create().show();
        }
        if (!PermissionUtils.canshowWindow(mContext)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("说明");
            builder.setMessage("我们需要获取后台打开页面和悬浮窗的权限，如果您不赋予此权限则无法使用该软件,点击确定跳转至获取权限的页面，请打开该权限");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(getAppDetailSettingIntent());
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.create().show();
            sharepreferenceApp.setSharepreference("rest", false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //加载已安装的app
        presenter.getInstalled(packageManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    /**
     * 加载已经安装的app并更新adapter
     *
     * @param list
     */
    @Override
    public void installedApp(List list) {
        this.list = list;
        adapter.update(list);
    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @return
     */
    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;

    }

}
