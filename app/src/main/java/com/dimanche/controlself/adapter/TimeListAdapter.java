package com.dimanche.controlself.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dimanche.controlself.R;
import com.dimanche.controlself.data.entity.TimeEntity;

import java.util.List;

public class TimeListAdapter extends BaseAdapter {

    private List<TimeEntity> list;
    Context mContext;
    LayoutInflater inflater;

    public TimeListAdapter(List<TimeEntity> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    MyHolder myHolder = null;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            myHolder = new MyHolder();
            convertView = inflater.inflate(R.layout.adapter_tim_list, null);
            myHolder.startTime = convertView.findViewById(R.id.start_time);
            myHolder.endTime = convertView.findViewById(R.id.end_time);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }
        myHolder.startTime.setText(list.get(position).getStartTime() == null ? "" : list.get(position).getStartTime().toString());
        myHolder.endTime.setText(list.get(position).getEndTime() == null ? "" : list.get(position).getEndTime().toString());
        return convertView;
    }

    class MyHolder {
        TextView startTime;
        TextView endTime;

    }
}
