package com.dimanche.controlself.module.appset.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dimanche.controlself.R;
import com.dimanche.controlself.base.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TimeTotalFragment extends BaseFragment {
    View view;
    EditText total;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_time_total, container, false);
        total = view.findViewById(R.id.total);
        return view;
    }

    /**
     * 供外界获取输入框中的内容
     *
     * @return
     */
    public int getTotal() {
        String totalStr = total.getText() == null ? "0" : total.getText().toString();

        return Integer.parseInt(totalStr);
    }

    /**
     * 供外界设置输入框中的内容
     *
     * @param totalStr
     */
    public void setTotal(String totalStr) {
        total.setText(totalStr);
    }
}
