package com.linecorp.menu.validate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.linecorp.menu.validate.adapter.ItemAreaExpandableListAdapter;
import com.linecorp.menu.validate.network.model.DataModel;
import com.linecorp.menu.validate.network.model.ItemAreaDataModel;
import com.linecorp.menu.validate.network.model.ItemAreaModel;
import com.linecorp.menu.validate.network.model.common.JsonModelList;

import java.util.ArrayList;
import java.util.TreeMap;

public class ItemAreaTabFragment extends Fragment {

    ItemAreaModel itemAreaModel;
    private JsonModelList<DataModel> dataModel;
    private String deviceType;

    public ItemAreaTabFragment(ItemAreaModel itemAreaModel, JsonModelList<DataModel> dataModel, String deviceTyp) {
        this.itemAreaModel =itemAreaModel;
        this.dataModel = dataModel;
        this.deviceType =deviceTyp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_area, container, false);

        if(itemAreaModel !=null){
            int version = itemAreaModel.version;

            TextView textView = view.findViewById(R.id.more_version);
            textView.setText(String.format("version : %s",version));
            ArrayList<ItemAreaDataModel> data = itemAreaModel.data;
            ExpandableListView expandableListView = (ExpandableListView)view.findViewById(R.id.debug_list_view);

            TreeMap<Integer,String> headerHashMap =  new TreeMap<Integer,String>();
            for(int i=0;i<data.size();i++){
                headerHashMap.put(i,data.get(i).position);
            }
            TreeMap<String,ArrayList<Integer>> childHashMap =  new TreeMap<String,ArrayList<Integer>>();
            for(int i=0;i<data.size();i++){
                childHashMap.put(data.get(i).position,data.get(i).ids);
            }

            ItemAreaExpandableListAdapter listAdapter = new ItemAreaExpandableListAdapter(getContext(),headerHashMap, childHashMap, dataModel,deviceType);

            expandableListView.setAdapter(listAdapter);
//            listAdapter.notifyDataSetChanged();
//            int count = listAdapter.getGroupCount();
//            for ( int i = 0; i < count; i++ ){
//                expandableListView.expandGroup(i);
//            }
//
        }


        return view;
    }
}
