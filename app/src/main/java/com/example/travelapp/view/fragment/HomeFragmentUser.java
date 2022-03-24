package com.example.travelapp.view.fragment;

import android.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.controller.homefragment.HomeFMController;
import com.example.travelapp.view.interfacefragment.InterfaceHomeFmView;


public class HomeFragmentUser extends BaseFragment implements InterfaceHomeFmView {
    private ImageView avatarHomeFragment, imgNotificationHomeFm;
    private TextView tvGoodMorningHomeFm, tvUserNameHomeFm, tvWelcomeHomeFr, tvSeeMoreHomeFm;
    private TextView tvDiscoverHomeFM;
    private SearchView searchViewHomeFr;
    private RecyclerView recycleViewCategories,recycleViewRecommended,recycleViewKnowYourWorld;
    private HomeFMController homeFMController;
    public HomeFragmentUser() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_user;
    }

    @Override
    public void initController() {
        homeFMController = new HomeFMController();
    }

    @Override
    public void initView(View view) {
        avatarHomeFragment = view.findViewById(R.id.avatar_home_fragment);
        imgNotificationHomeFm = view.findViewById(R.id.img_notification_homefm);
        tvGoodMorningHomeFm = view.findViewById(R.id.tvGoodMorning_HomeFm);
        tvUserNameHomeFm = view.findViewById(R.id.tvUserName_HomeFm);
        tvWelcomeHomeFr = view.findViewById(R.id.tvWelcomeHomeFr);
        tvSeeMoreHomeFm = view.findViewById(R.id.tv_seeMore_HomeFm);
        tvDiscoverHomeFM = view.findViewById(R.id.tv_discover_HomeFM);
        searchViewHomeFr = view.findViewById(R.id.searchViewHomeFr);
        recycleViewCategories = view.findViewById(R.id.recycleViewCategories);
        recycleViewRecommended = view.findViewById(R.id.recycleViewRecommended);
        recycleViewKnowYourWorld = view.findViewById(R.id.recycleViewKnowYourWorld);
    }

    @Override
    public void initData() {
        setTextForWelcome();
    }

    private void setTextForWelcome() {
        String text = "Khám phá một\nthế giới mới.";
        tvWelcomeHomeFr.setText(text);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void onLoadDataFail(String message) {

    }

    @Override
    public void onLoadDataSuccess(String message) {

    }
}