package com.example.travelapp.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {


    public BaseActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        initBaseController();
        initview(savedInstanceState);
        initData();
        initEvent();
        setAdjustScreen();
    }

    public abstract void setAdjustScreen();


    public abstract void initBaseController();
    public abstract int getLayoutResID();
    public abstract void initview(Bundle savedInstanceState);
    protected abstract void initData();
    protected abstract void initEvent();


}
