package com.example.travelapp.view.admin;

import android.util.Log;
import android.view.View;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;


public class ProfileAdminFragment extends BaseFragment {
    private final String TAG = "ProfileAdminFragment";

    public ProfileAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_profile_admin;
    }

    @Override
    public void initController() {


    }

    @Override
    public void initView(View view) {
        Log.d(TAG, "initView: ");
    }

    @Override
    public void initData() {
        Log.d(TAG, "initData: ");
    }

    @Override
    public void initEvent() {
        Log.d(TAG, "initEvent: ");

    }


}