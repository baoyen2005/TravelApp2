package com.example.travelapp.view.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.travelapp.R;
import com.example.travelapp.view.adapter.ViewPagerAdapterBottomNavi;
import com.example.travelapp.view.fragment.ChatFragmentUser;
import com.example.travelapp.view.fragment.FavoriteFragmentUser;
import com.example.travelapp.view.fragment.HomeFragmentUser;
import com.example.travelapp.view.fragment.ProfileFragmentUser;
import com.example.travelapp.view.fragment.SearchFragmentUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityUser extends AppCompatActivity {
    private ViewPagerAdapterBottomNavi adapterBottomNavi;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private SearchFragmentUser searchFragmentUser;
    private FavoriteFragmentUser favoriteFragmentUser;
    private HomeFragmentUser homeFragmentUser;
    private ChatFragmentUser chatFragmentUser;
    private ProfileFragmentUser profileFragmentUser;
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
        searchFragmentUser = new SearchFragmentUser();
        favoriteFragmentUser = new FavoriteFragmentUser();
        homeFragmentUser = new HomeFragmentUser();
        chatFragmentUser = new ChatFragmentUser();
        profileFragmentUser = new ProfileFragmentUser();
    }
    private void setBottomNavigation(){
        adapterBottomNavi.addFragment(searchFragmentUser);
        adapterBottomNavi.addFragment(favoriteFragmentUser);
        adapterBottomNavi.addFragment(homeFragmentUser);
        adapterBottomNavi.addFragment(chatFragmentUser);
        adapterBottomNavi.addFragment(profileFragmentUser);
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