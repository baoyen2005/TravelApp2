package com.example.travelapp.view.activity.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.travelapp.R;
import com.example.travelapp.view.adapter.ViewPagerAdapterBottomNavi;
import com.example.travelapp.view.fragment.ChatFragment;
import com.example.travelapp.view.fragment.FavoriteFragment;
import com.example.travelapp.view.fragment.HomeFragment;
import com.example.travelapp.view.fragment.ProfileFragment;
import com.example.travelapp.view.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ViewPagerAdapterBottomNavi adapterBottomNavi;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private SearchFragment searchFragment;
    private FavoriteFragment favoriteFragment;
    private HomeFragment homeFragment;
    private ChatFragment chatFragment;
    private ProfileFragment profileFragment;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initview();
        bottomNavigationView.setItemIconTintList(null);
        initFragment();

        setBottomNavigation();
        setFragmentForNavigation();
    }
    private void initview() {
        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavi);
        adapterBottomNavi = new ViewPagerAdapterBottomNavi(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }

    private void initFragment() {
        searchFragment = new SearchFragment();
        favoriteFragment= new FavoriteFragment();
        homeFragment = new HomeFragment();
        chatFragment = new ChatFragment();
        profileFragment = new ProfileFragment();
    }
    private void setBottomNavigation(){
        adapterBottomNavi.addFragment(searchFragment);
        adapterBottomNavi.addFragment(favoriteFragment);
        adapterBottomNavi.addFragment(homeFragment);
        adapterBottomNavi.addFragment(chatFragment);
        adapterBottomNavi.addFragment(profileFragment);
        viewPager.setAdapter( adapterBottomNavi);
         //menu = bottomNavigationView.getMenu();
    }
    private  void setFragmentForNavigation(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0: {
                     //   bottomNavigationView.getMenu().findItem(R.id.search_bottom_navi);
                        bottomNavigationView.getMenu().findItem(R.id.search_bottom_navi).setChecked(true);
                      //  bottomNavigationView.getMenu().findItem(R.id.search_bottom_navi).getIcon().set
                        break;
                    }
                    case 1: {
                        bottomNavigationView.getMenu().findItem(R.id.favor_bottom_navi).setChecked(true);
                        break;
                    }
                    case 3: {
                        bottomNavigationView.getMenu().findItem(R.id.chat_bottom_navi).setChecked(true);
                        break;
                    }
                    case 4: {
                        bottomNavigationView.getMenu().findItem(R.id.profile_bottom_navi).setChecked(true);
                        break;
                    }
                    default:{
                        bottomNavigationView.getMenu().findItem(R.id.home_bottom_navi).setChecked(true);
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search_bottom_navi:
                    {
                        viewPager.setCurrentItem(0);
                        return  true;
                    }
                    case R.id.favor_bottom_navi:
                    {
                        viewPager.setCurrentItem(1);
                        return  true;
                    }
                    case R.id.home_bottom_navi:{
                        viewPager.setCurrentItem(2);
                        return  true;
                    }
                    case R.id.chat_bottom_navi: {
                        viewPager.setCurrentItem(3);
                        return  true;
                    }
                    case R.id.profile_bottom_navi: {
                        viewPager.setCurrentItem(4);
                        return  true;
                    }
                    default:{
                        return false;
                    }
                }
            }
        });
    }

}