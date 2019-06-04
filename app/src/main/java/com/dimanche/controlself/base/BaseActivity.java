package com.dimanche.controlself.base;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dimanche.controlself.R;
import com.dimanche.controlself.utils.DialogUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;


/**
 * Created by dimanche on 2019/5/15.
 */

public class BaseActivity extends FragmentActivity implements BaseView {
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplication.setActivity(this);
        this.mContext = this;
        setImmersive();
    }


    protected void toast(String msg) {
        DialogUtils.showToast(this, msg);
    }

    /**
     * 适配状态栏为沉浸式
     */
    private void setImmersive() {
        // 去掉系统自带的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
    }

    /**
     * 设置标题行
     *
     * @param titleStr
     */
    protected void setTitle(Context mContext,String titleStr) {
        try {
            View view = LayoutInflater.from(mContext).inflate(R.layout.title_bar, null);
            TextView title = view.findViewById(R.id.title);
            ImageView bac = view.findViewById(R.id.bac);
            title.setText(titleStr);
            bac.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        } catch (Exception e) {
            toast("没有标题行");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AndroidApplication.remove(this);
    }
}
