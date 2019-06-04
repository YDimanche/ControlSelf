package com.dimanche.controlself.module.rest;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.dimanche.controlself.R;
import com.dimanche.controlself.base.BaseActivity;

import androidx.annotation.Nullable;

public class RestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}
