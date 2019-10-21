package com.linecorp.menu.validate.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.linecorp.menu.validate.R;
import com.linecorp.menu.validate.network.model.DataModel;
import com.linecorp.menu.validate.network.model.MoreTabModel;
import com.linecorp.menu.validate.network.model.common.JsonModelList;

import java.util.ArrayList;

public class MoreSubTabAdapter extends RecyclerView.Adapter<MoreSubTabAdapter.ViewHolder>{
        private ArrayList<Integer> listdata;
    MoreTabModel moreTabModel = null;
    JsonModelList<DataModel> mdataModel = null;
    String deviceType;
        public MoreSubTabAdapter(ArrayList<Integer> ids,  JsonModelList<DataModel> mdataModel,String deviceType) {
            this.listdata = ids;
            this.mdataModel =mdataModel;
            this.deviceType =deviceType;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.more_sub_tab_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final int ids = listdata.get(position);
            DataModel dataModel = findIconName(ids);

            holder.id.setText(ids+"");
            if(dataModel !=null){
                if(dataModel.title !=null){
                    holder.name.setText(dataModel.title );
                }else{
                    holder.name.setVisibility(View.GONE);
                }
                if(dataModel.iconUrlBase !=null){
                    Glide.with(holder.imageView.getContext())
                            .load(dataModel.iconUrlBase +"/"+deviceType+"/icon")
                            .into(holder.imageView);
                }
            }else{
                holder.name.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView id;
            public TextView name;
            public ViewHolder(View itemView) {
                super(itemView);
                this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
                this.id = (TextView) itemView.findViewById(R.id.id);
                this.name = (TextView) itemView.findViewById(R.id.name);
            }
        }

    private DataModel findIconName(int id){

        for(DataModel model :mdataModel){
            if(model.id == id){
                return model;
            }
        }
        return null;
    }

}
