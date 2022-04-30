package com.example.travelapp.view.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseActivity;
import com.example.travelapp.view.adapter.ViewPagerAdapterBottomNavi;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityAdmin extends BaseActivity {
    private ViewPagerAdapterBottomNavi adapterBottomNavi;
    private ViewPager viewPagerAdmin;
    private BottomNavigationView bottomNavigationAdmin;
    private RootAdminHomeFragment rootAdminHomeFragment;
    private ChatAdminFragment chatAdminFragment;
    private ProfileAdminFragment profileAdminFragment;

    @Override
    public void setAdjustScreen() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void initBaseController() {
        Log.d("TAG", "initBaseController: ");
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_main_admin;
    }

    @Override
    public void initview(Bundle savedInstanceState) {
        viewPagerAdmin = findViewById(R.id.viewPagerAdmin);
        bottomNavigationAdmin = findViewById(R.id.bottomNaviAdmin);
        bottomNavigationAdmin.setItemIconTintList(null);
    }

    @Override
    protected void initData() {
        adapterBottomNavi = new ViewPagerAdapterBottomNavi(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }

    @Override
    protected void initEvent() {
        initFragment();

        setBottomNavigation();
        setFragmentForNavigation();
    }

    private void initFragment() {
        rootAdminHomeFragment = new RootAdminHomeFragment();
        chatAdminFragment = new ChatAdminFragment();
        profileAdminFragment = new ProfileAdminFragment();
    }

    private void setBottomNavigation() {
        adapterBottomNavi.addFragment(rootAdminHomeFragment);
        adapterBottomNavi.addFragment(chatAdminFragment);
        adapterBottomNavi.addFragment(profileAdminFragment);

        viewPagerAdmin.setAdapter(adapterBottomNavi);
        //menu = bottomNavigationView.getMenu();
    }

    @SuppressLint("NonConstantResourceId")
    private void setFragmentForNavigation() {
        viewPagerAdmin.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1: {
                        bottomNavigationAdmin.getMenu().findItem(R.id.chat_bottom_navi_admin).setChecked(true);
                        break;
                    }
                    case 3: {
                        bottomNavigationAdmin.getMenu().findItem(R.id.profile_bottom_navi_admin).setChecked(true);
                        break;
                    }
                    default: {
                        bottomNavigationAdmin.getMenu().findItem(R.id.home_bottom_navi_admin).setChecked(true);
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationAdmin.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_bottom_navi_admin: {
                    viewPagerAdmin.setCurrentItem(0);
                    return true;
                }
                case R.id.chat_bottom_navi_admin: {
                    viewPagerAdmin.setCurrentItem(1);
                    return true;
                }
                case R.id.profile_bottom_navi_admin: {
                    viewPagerAdmin.setCurrentItem(2);
                    return true;
                }
                default: {
                    return false;
                }
            }
        });
    }

}