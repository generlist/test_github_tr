package com.linecorp.menu.validate;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.linecorp.menu.validate.network.model.DataModel;
import com.linecorp.menu.validate.network.model.MoreTabModel;
import com.linecorp.menu.validate.network.model.common.JsonModelList;

import java.util.ArrayList;

public class MoreTabFragment extends Fragment {



    MoreTabModel moreTabModel = null;
    JsonModelList<DataModel> mdataModel = null;
    GridView gridView= null;
    GridAdapter adapter = null;
    String deviceType="android";
    public MoreTabFragment(){

    }

    public static MoreTabFragment newInstance(MoreTabModel moreTabModel, JsonModelList<DataModel> dataModel,String deviceType) {

        Bundle args = new Bundle();
        MoreTabFragment fragment = new MoreTabFragment(moreTabModel,dataModel,deviceType);
        fragment.setArguments(args);
        return fragment;
    }
    private MoreTabFragment(MoreTabModel moreTabModel,JsonModelList<DataModel> dataList,String deviceType) {

        this.moreTabModel = moreTabModel;
        this.mdataModel =dataList;
        this.deviceType = deviceType;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view=  inflater.inflate(R.layout.fragment_more_tab, container, false);
        TextView textVersion = view.findViewById(R.id.more_version);
        TextView textSpec = view.findViewById(R.id.more_spec);
        if(moreTabModel !=null){
            textVersion.setText(String.format("version : %s",moreTabModel.version)+"");
            textSpec.setText(String.format("spec : %s",moreTabModel.spec+""));
        }
        gridView = (GridView)view. findViewById(R.id.more_grid);
        gridView.setNumColumns(3);
        if(moreTabModel.ids.size()>0){
            adapter = new GridAdapter(getContext(),moreTabModel.ids);
            gridView.setAdapter(adapter);
        }


        return view;
    }

    public class GridAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Integer> ids;

        public GridAdapter(Context context, ArrayList<Integer> ids) {
            this.context = context;
            this.ids = ids;
        }

        private DataModel findIconName(int id){

            for(DataModel model :mdataModel){
                if(model.id == id){
                    return model;
                }
            }
            return null;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View gridView;

            if (convertView == null) {

               int id = ids.get(position);
                gridView = inflater.inflate(R.layout.more_tab, null);

                TextView idTextView = (TextView) gridView.findViewById(R.id.gride_id);
                if (idTextView != null) {
                    idTextView.setText(id + "");
                }
                DataModel dataModel = findIconName(id);
                ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
                TextView itemName = (TextView) gridView.findViewById(R.id.grid_item_name);

                if(dataModel !=null){
                    if(dataModel.iconUrlBase !=null){

                        Glide.with(getActivity())
                                .load(dataModel.iconUrlBase +"/"+deviceType+"/icon")
                                .into(imageView);
                    }else{
                        imageView.setVisibility(View.GONE);
                    }

                    if(dataModel.title !=null){

                        itemName.setText(dataModel.title);
                    }else{
                        itemName.setVisibility(View.GONE);
                    }
                }else{
                    imageView.setVisibility(View.GONE);
                    itemName.setVisibility(View.GONE);
                }





            } else {
                gridView = (View) convertView;
            }

            return gridView;
        }

        @Override
        public int getCount() {
            return ids.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }








}
