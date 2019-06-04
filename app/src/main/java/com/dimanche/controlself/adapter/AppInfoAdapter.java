package com.dimanche.controlself.adapter;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dimanche.controlself.R;
import com.dimanche.controlself.data.entity.AppInfo;
import com.dimanche.controlself.utils.DialogUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.MyHolder> {

    Context mContext;
    List<AppInfo> list;
    PackageManager mPackageMannager;
    ApplicationInfo mApplicationInfo;
    private OnItemClickLitener mOnItemClickLitener;

    public AppInfoAdapter(Context mContext, List<AppInfo> list) {
        this.mContext = mContext;
        this.list = list;
        mPackageMannager = mContext.getPackageManager();

    }

    public void update(List<AppInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    /**
     * 创建viewholder，加载布局
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_appinfo, parent, false);
        return new MyHolder(view);
    }

    /**
     * 操作item
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        final AppInfo appInfo = list.get(position);
        if (appInfo != null) {
            try {
                if (mOnItemClickLitener != null) {
                    holder.line.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickLitener.onItemClick(v, position);
                        }
                    });
                }
                mApplicationInfo = mPackageMannager.getApplicationInfo(appInfo.getPackgeName(), PackageManager.GET_META_DATA);
                holder.mAppName.setText(appInfo.getAppName());
                holder.mAppIcon.setImageDrawable(mPackageMannager.getApplicationIcon(mApplicationInfo));
                holder.mCheckBox.setChecked("00".equals(appInfo.getIsMonitor()) ? true : false);
                holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.mCheckBox.isChecked()) {
                            list.get(position).setIsMonitor("00");
                        } else {
                            list.get(position).setIsMonitor("01");
                        }
                    }
                });
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                DialogUtils.showToast(mContext, "获取包图标异常");
            }
        }
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private ImageView mAppIcon;
        private TextView mAppName;
        private CheckBox mCheckBox;
        private RelativeLayout line;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mAppIcon = itemView.findViewById(R.id.app_icon);
            mAppName = itemView.findViewById(R.id.app_name);
            mCheckBox =  itemView.findViewById(R.id.checkbox);
            line = itemView.findViewById(R.id.line);

        }
    }

    /**
     * 点击事件的回调
     */
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }
}
