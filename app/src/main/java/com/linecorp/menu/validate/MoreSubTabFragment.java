package com.linecorp.menu.validate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.linecorp.menu.validate.adapter.MoreSubTabAdapter;
import com.linecorp.menu.validate.network.model.DataModel;
import com.linecorp.menu.validate.network.model.SUBMoreTabModel;
import com.linecorp.menu.validate.network.model.common.JsonModelList;

import java.util.ArrayList;

public class MoreSubTabFragment extends Fragment {

    SUBMoreTabModel moreSubTabModel;
    JsonModelList<DataModel> mdataModel = null;
    String deviceType="android";
    public MoreSubTabFragment(){

    }

    public static MoreSubTabFragment newInstance(SUBMoreTabModel moreSubTabModel, JsonModelList<DataModel> dataModel, String deviceType) {

        Bundle args = new Bundle();

        MoreSubTabFragment fragment = new MoreSubTabFragment(moreSubTabModel,dataModel,deviceType);
        fragment.setArguments(args);
        return fragment;
    }
    private MoreSubTabFragment(SUBMoreTabModel moreSubTabModel,JsonModelList<DataModel> dataModel, String deviceType) {
        this.moreSubTabModel =moreSubTabModel;
        this.deviceType =deviceType;
        this.mdataModel =dataModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sub_more, container, false);

        if(moreSubTabModel !=null){
            int version = moreSubTabModel.getVersion();
            String spec = moreSubTabModel.getSpec();

            TextView textView = view.findViewById(R.id.more_version);
            textView.setText(String.format("version : %s",version));
            TextView versionText = view.findViewById(R.id.more_spec);
            versionText.setText(String.format("spec : %s",spec));
            ArrayList<Integer> ids = moreSubTabModel.getIds();

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            MoreSubTabAdapter adapter = new MoreSubTabAdapter(ids,mdataModel,deviceType);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);

        }


        return view;
    }
}
