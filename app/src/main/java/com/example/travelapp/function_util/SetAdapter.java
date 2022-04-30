package com.example.travelapp.function_util;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SetAdapter {
    private Activity activity;


    public SetAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setAdapterRecycleViewHolderLinearlayout(RecyclerView.Adapter adapter, RecyclerView recycleView, int directLinearLayout){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity,
                directLinearLayout, false);
        recycleView.setLayoutManager(linearLayoutManager);
        recycleView.setAdapter(adapter);
    }

}
