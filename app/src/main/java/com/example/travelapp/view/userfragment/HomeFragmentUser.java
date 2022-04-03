package com.example.travelapp.view.userfragment;

import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME;
import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME_POST;

import android.content.Intent;
import android.util.Log;
import android.widget.SearchView;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.constant.UserHomeConstant;
import com.example.travelapp.controller.homefragment.HomeFMController;
import com.example.travelapp.function_util.GetAllPostFromFirebaseStorage;
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
    private ImageView avatarHomeFragment, imgNotificationHomeFm;
    private TextView tvGoodMorningHomeFm, tvUserNameHomeFm, tvWelcomeHomeFr, tvSeeMoreHomeFm;
    private TextView tvDiscoverHomeFM;
    private SearchView searchViewHomeFr;
    private RecyclerView recycleViewCategories, recycleViewRecommended, recycleViewKnowYourWorld;
    private HomeFMController homeFMController;
    private FirebaseAuth firebaseAuth;
    private UserHomeConstant userHomeConstant;

    private CategoryUserHomeAdapter categoryUserHomeAdapter;
    private RecommendUserHomeAdapter recommendUserHomeAdapter;
    private List<Category> categoryList = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();
    private GetAllPostFromFirebaseStorage getAllPostFromFirebaseStorage;
    private KnowYourWorldUserHomeAdapter knowYourWorldUserHomeAdapter;
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
        firebaseAuth = FirebaseAuth.getInstance();
        userHomeConstant = new UserHomeConstant();

        getCurrentUser();
        setCategoryRecycleView();
        setPostListForRecycleview();
    }

    private void setPostListForRecycleview() {
        getAllPostFromFirebaseStorage = new GetAllPostFromFirebaseStorage();
        getAllPostFromFirebaseStorage.getAllPostFromFirebase(new InterfaceEventGetPostListener() {
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
        knowYourWorldUserHomeAdapter = new KnowYourWorldUserHomeAdapter(list,requireContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recycleViewKnowYourWorld.setLayoutManager(linearLayoutManager);
        recycleViewKnowYourWorld.setAdapter(knowYourWorldUserHomeAdapter);
    }

    private void setCommendedRecycleview(List<Post> list) {
        recommendUserHomeAdapter = new RecommendUserHomeAdapter(list,requireContext(), this);
        Log.d(TAG_USER_HOME_POST, "setRecommendRecycleView: size postlist = "+ list.size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recycleViewRecommended.setLayoutManager(linearLayoutManager);
        recycleViewRecommended.setAdapter(recommendUserHomeAdapter);
        Log.d(TAG_USER_HOME_POST, "getPostsSuccess: homefmcontroller"+list.get(0).getTouristName());
    }

    private void setCategoryRecycleView() {
        categoryList = setCategoryList();
        categoryUserHomeAdapter = new CategoryUserHomeAdapter( categoryList, requireActivity());
        Log.d(TAG_USER_HOME, "setCategoryAdapter: categoryList = "+categoryList.size());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recycleViewCategories.setLayoutManager(linearLayoutManager);
        recycleViewCategories.setAdapter(categoryUserHomeAdapter);
    }

    private List<Category> setCategoryList() {
        List<Category> list = new ArrayList<>();
        Category category1 = new Category();
        category1.setTitleCate("Núi");
        category1.setDrawableImage(R.drawable.img_moutain);
        list.add(category1);

        Category category2 = new Category();
        category2.setDrawableImage(R.drawable.img_camping);
        category2.setTitleCate("Cắm trại");
        list.add(category2);

        Category category3 = new Category();
        category3.setTitleCate("biển");
        category3.setDrawableImage(R.drawable.img_beach);
        list.add(category3);

        Category category4 = new Category();
        category4.setDrawableImage(R.drawable.img_temple);
        category4.setTitleCate("Đền chùa");
        list.add(category4);
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

        DetailFragmentHomeUser detailFragmentHomeUser = DetailFragmentHomeUser.newInstance(post.getPostId());

        FragmentTransaction trans = getFragmentManager()
                .beginTransaction();
        trans.replace(R.id.root_home_frame,detailFragmentHomeUser);


        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);

        trans.commit();
    }
}