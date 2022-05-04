package com.example.travelapp.view.userfragment;

import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME_POST;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.controller.homefragment.HomeFMController;
import com.example.travelapp.function_util.ClickitemShowDetail;
import com.example.travelapp.function_util.GetPostFromFirebaseStorage;
import com.example.travelapp.function_util.SetAdapter;
import com.example.travelapp.function_util.SetCategoryListUtil;
import com.example.travelapp.model.Category;
import com.example.travelapp.model.Post;
import com.example.travelapp.view.adapter.user.CategoryUserHomeAdapter;
import com.example.travelapp.view.adapter.user.KnowYourWorldUserHomeAdapter;
import com.example.travelapp.view.adapter.user.RecommendUserHomeAdapter;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostListener;
import com.example.travelapp.view.interfacefragment.InterfaceHomeFmView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class HomeFragmentUser extends BaseFragment implements InterfaceHomeFmView , RecommendUserHomeAdapter.OnItemRecommendClickListener{
    private ImageView avatarHomeFragment;
    private TextView tvGoodMorningHomeFm, tvUserNameHomeFm, tvWelcomeHomeFr;
    private SearchView searchViewHomeFr;
    private RecyclerView recycleViewCategories, recycleViewRecommended, recycleViewKnowYourWorld;
    private HomeFMController homeFMController;
    private FirebaseAuth firebaseAuth;
    private SetAdapter setAdapter;
    private final List<Post> postList = new ArrayList<>();
    private SetCategoryListUtil setCategoryListUtil;
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
        searchViewHomeFr = view.findViewById(R.id.searchViewHomeFr);
        recycleViewCategories = view.findViewById(R.id.recycleViewCategories);
        recycleViewRecommended = view.findViewById(R.id.recycleViewRecommended);
        recycleViewKnowYourWorld = view.findViewById(R.id.recycleViewKnowYourWorld);
    }

    @Override
    public void initData() {
        firebaseAuth = FirebaseAuth.getInstance();
        setCategoryListUtil = new SetCategoryListUtil();
        setAdapter = new SetAdapter(requireActivity());
        getCurrentUser();
        setCategoryRecycleView();
        setPostListForRecyclerview();
    }

    private void setPostListForRecyclerview() {
        GetPostFromFirebaseStorage getPostFromFirebaseStorage = new GetPostFromFirebaseStorage();
        getPostFromFirebaseStorage.getAllPostFromFirebase(new InterfaceEventGetPostListener() {
            @Override
            public void getAllPostSuccess(List<Post> postListIn) {
                postList.clear();
                postList.addAll(postListIn);
                setCommendedRecycleview(postList);
                setKnowWorldRecycleView(postList);

            }

            @Override
            public void getPostsFail(String isFail) {
                Log.d(TAG_USER_HOME_POST, "getPostsFail: homefmcontroller");
            }
        });


    }

    private void setKnowWorldRecycleView(List<Post> list) {
        KnowYourWorldUserHomeAdapter knowYourWorldUserHomeAdapter = new KnowYourWorldUserHomeAdapter(list, requireContext());
        setAdapter.setAdapterRecycleViewHolderLinearlayout(knowYourWorldUserHomeAdapter,recycleViewKnowYourWorld, LinearLayoutManager.HORIZONTAL);
    }

    private void setCommendedRecycleview(List<Post> list) {
        RecommendUserHomeAdapter recommendUserHomeAdapter = new RecommendUserHomeAdapter(list, requireContext(), this);
        setAdapter.setAdapterRecycleViewHolderLinearlayout(recommendUserHomeAdapter,recycleViewRecommended, LinearLayoutManager.HORIZONTAL);
    }

    private void setCategoryRecycleView() {
        List<Category> categoryList = setCategoryList();
        CategoryUserHomeAdapter categoryUserHomeAdapter = new CategoryUserHomeAdapter(categoryList, requireActivity());
        setAdapter.setAdapterRecycleViewHolderLinearlayout(categoryUserHomeAdapter,recycleViewCategories, LinearLayoutManager.HORIZONTAL);
    }
    
    private List<Category> setCategoryList() {
        List<Category> list = new ArrayList<>();
        list.add(setCategoryListUtil.setCategoryList("Núi",R.drawable.img_moutain));
        list.add(setCategoryListUtil.setCategoryList("Cắm trại",R.drawable.img_camping));
        list.add(setCategoryListUtil.setCategoryList("Biển",R.drawable.img_beach));
        list.add(setCategoryListUtil.setCategoryList("Đền chùa",R.drawable.img_temple));
        return list;
    }

    private void getCurrentUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        homeFMController.onLoadInformationUser(requireContext(),firebaseUser, tvUserNameHomeFm,avatarHomeFragment);

    }



    private void setTextForWelcome() {
        String text = "Khám phá một\nthế giới mới.";
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

    @Override
    public void onItemClick(Post post, int position) {

        String tag = "HomeFragment";
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        ClickitemShowDetail clickitemShowDetail = new ClickitemShowDetail();
        clickitemShowDetail.ClickItemShowDetailInfor(post.getPostId(),R.id.root_home_frame,fragmentManager, tag);
    }
}