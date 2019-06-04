package com.dimanche.controlself.module.appset.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;

import com.dimanche.controlself.R;
import com.dimanche.controlself.adapter.TimeListAdapter;
import com.dimanche.controlself.base.BaseFragment;
import com.dimanche.controlself.data.entity.TimeEntity;
import com.dimanche.controlself.module.appset.AppSetActivity;
import com.dimanche.controlself.utils.DialogUtils;
import com.dimanche.controlself.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Dimanche
 */
public class TimeAlotFragment extends BaseFragment {
    View view;
    //listview相关
    ListView listView;
    TimeListAdapter adapter;
    List<TimeEntity> list;//用于存放时间的集合

    //父控件
    ScrollView scrollView;

    //确定按钮及开始结束时间
    EditText startTime, endTime;
    Button confirm;
    int startHour, startMinu;
    int endHour, endMinu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_time_slot, container, false);
        //listview相关
        if (AppSetActivity.timeList != null) {
            list = AppSetActivity.timeList;
        } else {
            list = new ArrayList<>();
        }

        listView = view.findViewById(R.id.list);
        adapter = new TimeListAdapter(list, getActivity());
        listView.setAdapter(adapter);

        //确定按钮及开始结束时间
        startTime = view.findViewById(R.id.start_time_tv);
        endTime = view.findViewById(R.id.end_time_tv);
        confirm = view.findViewById(R.id.confirm);


        scrollView = getActivity().findViewById(R.id.scroll);
        //解决在滚动视图中嵌套listview，listview不能滚动的问题
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    // 当手指触摸listview时，让父控件焦点,不能滚动
                    case MotionEvent.ACTION_DOWN:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                    case MotionEvent.ACTION_MOVE:
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        // 当手指松开时，让父控件重新获取焦点
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("确定？");
                builder.setMessage("确定要删除该条时间段？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return false;
            }
        });


        //开始时间
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showTimerPickerDialog(getActivity(), "请选择开始时间", 21, 33, new DialogUtils.OnTimerPickerListener() {
                    @Override
                    public void onConfirm(int hourOfDay, int minute) {
                        startTime.setText(hourOfDay + ":" + minute);
                        startHour = hourOfDay;
                        startMinu = minute;
                    }
                });


            }
        });

        //结束时间
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showTimerPickerDialog(getActivity(), "请选择结束时间", 21, 33, new DialogUtils.OnTimerPickerListener() {
                    @Override
                    public void onConfirm(int hourOfDay, int minute) {
                        endTime.setText(hourOfDay + ":" + minute);
                        endHour = hourOfDay;
                        endMinu = minute;
                    }
                });
            }
        });
        //确定
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable startStr = startTime.getText();
                Editable endStr = endTime.getText();
                if (startStr == null || startStr.length() == 0) {
                    toast("请选择开始时间");
                    return;
                }
                if (endStr == null || endStr.length() == 0) {
                    toast("请选择结束时间");
                    return;
                }
                //结束时间是否大于开始时间
                if (endHour < startHour) {
                    toast("结束时间应该大于开始时间");
                    return;
                }
                if (endHour == startHour) {
                    if (endMinu <= startMinu) {
                        toast("结束时间应该大于开始时间");
                        return;
                    }
                }

                TimeEntity entity = new TimeEntity();
                entity.setStartTime(startStr.toString());
                entity.setEndTime(endStr.toString());
                list.add(entity);
                adapter.notifyDataSetChanged();
                startTime.setText("");
                endTime.setText("");

            }
        });
        return view;
    }

    /**
     * 供外界获取设置的时间段
     *
     * @return
     */
    public List<TimeEntity> getList() {
        return list;
    }


}
