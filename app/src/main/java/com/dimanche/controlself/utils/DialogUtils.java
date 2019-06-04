package com.dimanche.controlself.utils;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.TimePicker;
import android.widget.Toast;


/**
 * 弹出框工具类
 *
 * @author diamnche
 */
public class DialogUtils {

    /**
     * toast
     *
     * @param context 上下文
     * @param msg     提示信息
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 选择时间弹出框
     *  @param context
     * @param title
     * @param hourOfDay
     * @param minute
     * @param onTimerPickerListener
     */
    public static void showTimerPickerDialog(Context context, String title, int hourOfDay, int minute,final OnTimerPickerListener onTimerPickerListener) {
        TimePickerDialog dialog = new TimePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (onTimerPickerListener != null) {
                    onTimerPickerListener.onConfirm(hourOfDay, minute);
                }
            }
        }, hourOfDay, minute, true);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        dialog.show();
    }

    /**
     * 时间选择器监听
     */
    public interface OnTimerPickerListener {
        /**
         * 确定
         * @param hourOfDay
         * @param minute
         */
        void onConfirm(int hourOfDay, int minute);

    }

}
