package com.linecorp.menu.validate.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.linecorp.menu.validate.CategoryFragment;
import com.linecorp.menu.validate.ItemAreaTabFragment;
import com.linecorp.menu.validate.MoreSubTabFragment;
import com.linecorp.menu.validate.MoreTabFragment;
import com.linecorp.menu.validate.network.model.MenuModel;

public class FragmentAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    MenuModel menuModel = null;
    String deviceType = null;



    public FragmentAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MoreTabFragment moreTabFragment = MoreTabFragment.newInstance(menuModel.getMoreTabList(),menuModel.getData(),deviceType);
                return moreTabFragment;
            case 1:
                MoreSubTabFragment moreSubTabFragment = MoreSubTabFragment.newInstance(menuModel.getSubMoreTabList(),menuModel.getData(),deviceType);
                return moreSubTabFragment;
            case 2:
                CategoryFragment categoryFragment = new CategoryFragment(menuModel.getCategoryList(),menuModel.getData(),deviceType);
                return categoryFragment;
            case 3:
                ItemAreaTabFragment itemAreaFragment = new ItemAreaTabFragment(menuModel.getItemArea(),menuModel.getData(),deviceType);
                return itemAreaFragment;
            default:
                return null;
        }
    }

    public void setMenuModel(MenuModel menuModel,String deviceType) {
        this.menuModel = menuModel;
        this.deviceType =deviceType;
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
