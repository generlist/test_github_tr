package com.linecorp.menu.validate.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linecorp.menu.validate.R;
import com.linecorp.menu.validate.network.model.DataModel;
import com.linecorp.menu.validate.network.model.common.JsonModelList;

import java.util.ArrayList;
import java.util.TreeMap;

public class ItemAreaExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    TreeMap<String,ArrayList<Integer>> childMap;
    private JsonModelList<DataModel> dataModel;
    private String deviceType;
    private TreeMap<Integer,String> headerHashMap;

    public ItemAreaExpandableListAdapter(Context context, TreeMap<Integer,String> headerHashMap , TreeMap<String,ArrayList<Integer>> childMap , JsonModelList<DataModel> dataModel, String deviceType) {
        this._context = context;
        this.dataModel = dataModel;
        this.headerHashMap = headerHashMap;
        this.childMap = childMap;
        this.deviceType =deviceType;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return childMap.get(this.headerHashMap.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expand_view_child, null);
        }

        TextView id = (TextView) convertView.findViewById(R.id.id);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);


        int ids = (Integer) getChild(groupPosition, childPosition);
        id.setText(ids+"");

        DataModel dataModel = findIconName(ids);
        if(dataModel !=null){
           if(dataModel.title !=null){
               name.setText(dataModel.title);
           }else{
               name.setVisibility(View.GONE);
           }
            if (dataModel.iconUrlBase != null) {

                Glide.with(imageView.getContext())
                        .load(dataModel.iconUrlBase + "/" + deviceType + "/icon")
                        .into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }


        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        if(this.childMap.get(this.headerHashMap.get(groupPosition)) != null) {
            Log.e("hbungshin","size :" + this.childMap.get(this.headerHashMap.get(groupPosition)).size());
            return this.childMap.get(this.headerHashMap.get(groupPosition)).size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {

        if(headerHashMap.get(groupPosition) !=null){
            return headerHashMap.get(groupPosition);
        }

        return null;

    }

    @Override
    public int getGroupCount() {
        if(headerHashMap !=null){
            return this.headerHashMap.size();
        }
        return 0;

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String position = (String)getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expand_view_group, null);
        }

        TextView groupNmae = (TextView) convertView.findViewById(R.id.groupNaName);

        if(groupNmae !=null){
            groupNmae.setText(String.format("[position] :  %s",position));
        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private DataModel findIconName(int id){

        for(DataModel model :dataModel){
            if(model.id == id){
                return model;
            }
        }
        return null;
    }

}
