package com.example.travelapp.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapterBottomNavi extends FragmentPagerAdapter {
    public ViewPagerAdapterBottomNavi(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapterBottomNavi(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    private ArrayList<Fragment> mFragmentList = new ArrayList();
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    public void addFragment( Fragment fragment){
        mFragmentList.add(fragment);


    }
}
