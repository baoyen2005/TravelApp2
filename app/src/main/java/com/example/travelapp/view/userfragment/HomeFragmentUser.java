package com.example.travelapp.view.userfragment;

import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME_POST;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.broadcast.NotificationService;
import com.example.travelapp.controller.homefragment.HomeFMController;
import com.example.travelapp.function_util.ClickitemShowDetail;
import com.example.travelapp.function_util.GetPostFromFirebaseStorage;
import com.example.travelapp.function_util.SetAdapter;
import com.example.travelapp.function_util.SetCategoryListUtil;
import com.example.travelapp.model.Category;
import com.example.travelapp.model.Post;
import com.example.travelapp.view.adapter.user.CategoryUserHomeAdapter;
import com.example.travelapp.view.adapter.user.RecommendUserHomeAdapter;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostListener;
import com.example.travelapp.view.interfacefragment.InterfaceHomeFmView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class HomeFragmentUser extends BaseFragment implements InterfaceHomeFmView,
        RecommendUserHomeAdapter.OnItemRecommendClickListener {
    private ImageView avatarHomeFragment;
    private TextView tvGoodMorningHomeFm, tvUserNameHomeFm, tvWelcomeHomeFr;
    private RecyclerView recycleViewCategories, recycleViewRecommended;
    private HomeFMController homeFMController;
    private FirebaseAuth firebaseAuth;
    private SetAdapter setAdapter;
    private final List<Post> postList = new ArrayList<>();
    private SetCategoryListUtil setCategoryListUtil;
    private ImageSlider imageSliderInUserHome;
    private ArrayList<SlideModel> slideModels;
    HashMap<String, String> Hash_file_maps;

    public HomeFragmentUser() {
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
        tvGoodMorningHomeFm = view.findViewById(R.id.tvGoodMorning_HomeFm);
        tvUserNameHomeFm = view.findViewById(R.id.tvCurrentUserNameInUserHomeScreen);
        tvWelcomeHomeFr = view.findViewById(R.id.tvWelcomeHomeFr);
        SearchView searchViewHomeFr = view.findViewById(R.id.searchViewHomeFr);
        recycleViewCategories = view.findViewById(R.id.recycleViewCategories);
        recycleViewRecommended = view.findViewById(R.id.recycleViewRecommended);
        imageSliderInUserHome = view.findViewById(R.id.imageSliderInUserHome);

    }

    @Override
    public void initData() {
        firebaseAuth = FirebaseAuth.getInstance();
        setCategoryListUtil = new SetCategoryListUtil();
        setAdapter = new SetAdapter(requireActivity());
        Hash_file_maps = new HashMap<String, String>();
        slideModels = new ArrayList<>();
        getCurrentUser();
        setCategoryRecycleView();
        setPostListForRecyclerview();
        setNotificationReminder();
    }

    private void setPostListForRecyclerview() {
        GetPostFromFirebaseStorage getPostFromFirebaseStorage = new GetPostFromFirebaseStorage();
        getPostFromFirebaseStorage.getAllPostFromFirebase(new InterfaceEventGetPostListener() {
            @Override
            public void getAllPostSuccess(List<Post> postListIn) {
                postList.clear();
                postList.addAll(postListIn);
                setCommendedRecycleview(postList);
                for (Post post : postListIn) {
                    Log.d("__slider", "getAllPostSuccess: " + post.getList_photos().get(0).getUrl());
                    slideModels.add(new SlideModel(post.getList_photos().get(0).getUrl(), ScaleTypes.FIT));

                }
                Log.d("__slider", "getAllPostSuccess: " + slideModels.size());
                imageSliderInUserHome.setImageList(slideModels, ScaleTypes.FIT);


            }

            @Override
            public void getPostsFail(String isFail) {
                Log.d(TAG_USER_HOME_POST, "getPostsFail: homefmcontroller");
            }
        });


    }


    private void setCommendedRecycleview(List<Post> list) {
        RecommendUserHomeAdapter recommendUserHomeAdapter = new RecommendUserHomeAdapter(list, requireContext(), this);
        setAdapter.setAdapterRecycleViewHolderLinearlayout(recommendUserHomeAdapter, recycleViewRecommended, LinearLayoutManager.HORIZONTAL);
    }

    private void setCategoryRecycleView() {
        List<Category> categoryList = setCategoryList();
        CategoryUserHomeAdapter categoryUserHomeAdapter = new CategoryUserHomeAdapter(categoryList, requireActivity());
        setAdapter.setAdapterRecycleViewHolderLinearlayout(categoryUserHomeAdapter, recycleViewCategories, LinearLayoutManager.HORIZONTAL);
    }

    private List<Category> setCategoryList() {
        List<Category> list = new ArrayList<>();
        list.add(setCategoryListUtil.setCategoryList("mountain", R.drawable.img_moutain));
        list.add(setCategoryListUtil.setCategoryList("camping", R.drawable.img_camping));
        list.add(setCategoryListUtil.setCategoryList("beach", R.drawable.img_beach));
        list.add(setCategoryListUtil.setCategoryList("temple", R.drawable.img_temple));
        return list;
    }

    private void getCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        homeFMController.onLoadInformationUser(requireContext(), firebaseUser, tvUserNameHomeFm, avatarHomeFragment);

    }


    private void setTextForWelcome() {
        String text = "Explore\nnew world.";
        tvWelcomeHomeFr.setText(text);
    }

    @Override
    public void initEvent() {
        setTextForWelcome();

        homeFMController.setTextForTVGoodmorning(tvGoodMorningHomeFm);
    }


    @Override
    public void onLoadDataFail(String message) {

    }

    @Override
    public void onLoadDataSuccess(String message) {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void setNotificationReminder() {
        int notificationId = 1;
        Intent intent = new Intent(requireActivity(), NotificationService.class);
        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(requireActivity(),0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        intent.putExtra("notificationId", notificationId);
        intent.putExtra("message", "Travel it now");
        int hour = 11;
        int minute =0;
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, hour);
        startTime.set(Calendar.MINUTE, minute);
        startTime.set(Calendar.SECOND, 0);
        long alarmStartTime = startTime.getTimeInMillis();
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                alarmStartTime,
                1000 * 60 * 60 * 24,
                pendingIntent
        );
    }

    @Override
    public void onItemClick(Post post, int position) {

        String tag = "HomeFragment";
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        ClickitemShowDetail clickitemShowDetail = new ClickitemShowDetail();
        clickitemShowDetail.ClickItemShowDetailInfor(post.getPostId(), R.id.root_home_frame, fragmentManager, tag);
    }


}