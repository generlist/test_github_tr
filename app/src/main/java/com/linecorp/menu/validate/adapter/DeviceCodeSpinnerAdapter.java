package com.linecorp.menu.validate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.linecorp.menu.validate.R;
import com.linecorp.menu.validate.data.MenuParameterItem;
import com.linecorp.menu.validate.data.StaticMenuMeta;

import java.util.List;

public class DeviceCodeSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context mContext= null;
    private List<StaticMenuMeta.DeviceItem> mDataList;

    public DeviceCodeSpinnerAdapter(Context context, List<StaticMenuMeta.DeviceItem> mDataList) {

        this.mDataList = mDataList;
        this.mContext = context;
    }

    public int getCount() {

        return mDataList.size();

    }

    public Object getItem(int position) {

        return mDataList.get(position);
    }

    public long getItemId(int position) {
        return (long)position;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        final DeviceCodeSpinnerAdapter.HolderVideoItem holderVideoItem;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spiner_item, parent, false);
            holderVideoItem = new DeviceCodeSpinnerAdapter.HolderVideoItem();
            holderVideoItem.textViewItemName = convertView.findViewById(R.id.spiner_clip_item);
            convertView.setTag(holderVideoItem);


        } else {
            holderVideoItem = (DeviceCodeSpinnerAdapter.HolderVideoItem) convertView.getTag();
        }
        StaticMenuMeta.DeviceItem services = mDataList.get(position);

        holderVideoItem.textViewItemName.setText(((StaticMenuMeta.DeviceItem) services).title);

        return convertView;
    }


    public View getView(int position, View convertView, ViewGroup viewgroup) {

        final DeviceCodeSpinnerAdapter.HolderVideoItem holderVideoItem;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.spiner_item, viewgroup, false);

            holderVideoItem = new DeviceCodeSpinnerAdapter.HolderVideoItem();
            holderVideoItem.textViewItemName = convertView.findViewById(R.id.spiner_clip_item);
            convertView.setTag(holderVideoItem);

        } else {
            holderVideoItem = (DeviceCodeSpinnerAdapter.HolderVideoItem) convertView.getTag();
        }

        StaticMenuMeta.DeviceItem services = mDataList.get(position);
        holderVideoItem.textViewItemName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
        holderVideoItem.textViewItemName.setText(services.title);

        return convertView;

    }

    private class HolderVideoItem {
        private TextView textViewItemName;
    }
}
