package com.cmlanche.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmlanche.core.utils.Constant;
import com.cmlanche.jixieshou.R;
import com.cmlanche.model.AppInfo;

import java.util.List;

public class AppListAdapter extends BaseAdapter {

    private List<AppInfo> appInfos;
    private LayoutInflater inflater;

    public AppListAdapter(Context context, List<AppInfo> appInfos) {
        this.inflater = LayoutInflater.from(context);
        this.appInfos = appInfos;
    }

    @Override
    public int getCount() {
        return appInfos == null ? 0 : appInfos.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return appInfos == null ? null : appInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppListViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_typeitem, null);
            holder = new AppListViewHolder();
            holder.icon = convertView.findViewById(R.id.icon);
            holder.name = convertView.findViewById(R.id.name);
            holder.free = convertView.findViewById(R.id.free);
            convertView.setTag(holder);
        } else {
            holder = (AppListViewHolder) convertView.getTag();
        }
        AppInfo info = getItem(position);
        holder.name.setText(info.getName());
        if (info.isFree()) {
            holder.free.setText("免费");
        } else {
            holder.free.setText("VIP");
        }
        holder.name.setText(info.getName());
        if (info.getPkgName().equals(Constant.PN_DOU_YIN)) {
            holder.icon.setImageResource(R.drawable.dy_fast);
        } else if (info.getPkgName().equals(Constant.PN_KUAI_SHOU)) {
            holder.icon.setImageResource(R.drawable.ks_fast);
        } else if (info.getPkgName().equals(Constant.PN_DOU_YIN)) {
            holder.icon.setImageResource(R.drawable.dy);
        }else if (info.getPkgName().equals(Constant.PN_TOU_TIAO)) {
            holder.icon.setImageResource(R.drawable.icon_toutiao);
        }else if (info.getPkgName().equals(Constant.PN_FENG_SHENG)) {
            holder.icon.setImageResource(R.drawable.icon_fengsheng);
        }else if (info.getPkgName().equals(Constant.PN_DIAN_TAO)) {
            holder.icon.setImageResource(R.drawable.icon_diantao);
        }else if (info.getPkgName().equals(Constant.PN_YING_KE)) {
            holder.icon.setImageResource(R.drawable.icon_yingke);
        }
        return convertView;
    }

    private class AppListViewHolder {
        ImageView icon;
        TextView name;
        TextView free;
    }
}
