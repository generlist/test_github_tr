package com.linecorp.menu.validate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.linecorp.menu.validate.adapter.CategoryExpandableListAdapter;
import com.linecorp.menu.validate.network.model.CategoryDataModel;
import com.linecorp.menu.validate.network.model.CategoryListModel;
import com.linecorp.menu.validate.network.model.DataModel;
import com.linecorp.menu.validate.network.model.common.JsonModelList;

import java.util.ArrayList;
import java.util.HashMap;

public class CategoryFragment extends Fragment {

    private CategoryListModel categoryListModel;
    private JsonModelList<DataModel> dataModel;
    private String deviceType;

    public CategoryFragment(CategoryListModel categoryListModel, JsonModelList<DataModel> dataModel, String deviceType) {
        this.categoryListModel =categoryListModel;
        this.dataModel = dataModel;
        this.deviceType = deviceType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        if(categoryListModel !=null){
            int version = categoryListModel.version;
            String spec = categoryListModel.spec;

            TextView textView = view.findViewById(R.id.more_version);
            textView.setText(String.format("version : %s",version));
            TextView versionText = view.findViewById(R.id.more_spec);
            versionText.setText(String.format("spec : %s",spec));
            ArrayList<CategoryDataModel> data = categoryListModel.data;
            ExpandableListView expandableListView = (ExpandableListView)view.findViewById(R.id.debug_list_view);


            HashMap<Integer,Integer> headerHashMap =  new HashMap<Integer,Integer>();
            for(int i=0;i<data.size();i++){
                headerHashMap.put(i,data.get(i).parentId);
            }
            HashMap<Integer,ArrayList<Integer>> childHashMap =  new HashMap<Integer,ArrayList<Integer>>();
            for(int i=0;i<data.size();i++){
                childHashMap.put(data.get(i).parentId,data.get(i).ids);
            }


            CategoryExpandableListAdapter listAdapter = new CategoryExpandableListAdapter(getContext(),headerHashMap, childHashMap, dataModel,deviceType);

            expandableListView.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
            int count = listAdapter.getGroupCount();
            for ( int i = 0; i < count; i++ ){
                expandableListView.expandGroup(i);
            }
        }


        return view;
    }


}
