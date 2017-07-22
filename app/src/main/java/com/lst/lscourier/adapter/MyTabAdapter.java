package com.lst.lscourier.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */

public class MyTabAdapter extends FragmentPagerAdapter {

    private List<String> list_Title;                              //tab名的列表
    private List<Fragment> fragments;

    public MyTabAdapter(FragmentManager fm, List<String> list_title, List<Fragment> fragments) {
        super(fm);
        this.list_Title = list_title;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return list_Title.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {

        return list_Title.get(position % list_Title.size());
    }


}
