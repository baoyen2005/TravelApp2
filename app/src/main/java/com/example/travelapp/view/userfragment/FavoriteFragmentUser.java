package com.example.travelapp.view.userfragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.controller.userfavoritepost.FavoritePostController;
import com.example.travelapp.function_util.ClickitemShowDetail;
import com.example.travelapp.function_util.SetAdapter;
import com.example.travelapp.model.FavoritePost;
import com.example.travelapp.view.adapter.user.UserFavoriteListPostAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FavoriteFragmentUser extends BaseFragment implements
        UserFavoriteListPostAdapter.OnItemFavoritePostClickListener {
    private UserFavoriteListPostAdapter userFavoriteListPostAdapter;
    private SearchView searchViewFavorite;
    private ImageView imgSearchFavorite;
    private FavoritePostController favoritePostController;
    private TextView tvTittleFavor, tvInWorld;
    private SetAdapter setAdapter;
    private RecyclerView recycleViewUserFavoriteFM;
    private final String TAG = "FavoriteFragmentUser";
    private List<FavoritePost> favoritePostList;
    private String uid;
    public FavoriteFragmentUser() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_favorite_user;
    }

    @Override
    public void initController() {
        favoritePostController = new FavoritePostController();

    }
    
    @Override
    public void initView(View view) {

        recycleViewUserFavoriteFM = view.findViewById(R.id.recycleViewUserFavoriteFM);
        searchViewFavorite =view.findViewById(R.id.searchViewFavorite);
        tvInWorld = view.findViewById(R.id.tvInWorld);
        tvTittleFavor = view.findViewById(R.id.tvTittleFavor);
    }



    @Override
    public void initData() {
        favoritePostList = new ArrayList<>();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        setAdapter = new SetAdapter(requireActivity());
    }

    @Override
    public void initEvent() {
        setPostListForRecyclerview();
        searchViewByName();
    }

    private void searchViewByName() {
            searchViewFavorite.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    if(s == null || s.isEmpty()){
                        Log.d(TAG, "onQueryTextSubmit: if null s = " +s);
                        Log.d(TAG, "onQueryTextSubmit: if null favoritePostList =  "+favoritePostList.size());
                        userFavoriteListPostAdapter.updateData(favoritePostList);
                    }
                    else{
                        Log.d(TAG, "onQueryTextSubmit: else !=null "+ s);
                        userFavoriteListPostAdapter.getFilter().filter(s);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if(s == null || s.isEmpty()){
                        Log.d(TAG, "onQueryTextChange: if s null "+s);
                        Log.d(TAG, "onQueryTextChange: if null favoritePostList =  "+favoritePostList.size());
                        userFavoriteListPostAdapter.updateData(favoritePostList);
                    }
                    else{
                        Log.d(TAG, "onQueryTextChange: else !=null "+ s);
                        userFavoriteListPostAdapter.getFilter().filter(s);
                    }
                    return false;
                }
            });

            searchViewFavorite.setOnCloseListener(() -> {
                searchViewFavorite.setIconified(false);
                imgSearchFavorite.setVisibility( View.VISIBLE);
                searchViewFavorite.setVisibility( View.INVISIBLE);
                userFavoriteListPostAdapter.updateData(favoritePostList);
                Log.d(TAG, "searchViewByName:close favoritePostList.size()"+ favoritePostList.size());
                return false;
            });

    }

    private void setPostListForRecyclerview() {
      favoritePostController.getAllFavoritePostFromFirebase(requireActivity(), uid, new FavoritePostController.OnLoadDataListener() {
          @Override
          public void onSuccess(List<FavoritePost> list) {
              favoritePostList.clear();
              setFavoriteRecyclerview(list);
              favoritePostList.addAll(list);
          }

          @Override
          public void onFailure() {

          }

          @Override
          public void onAuthNull() {

          }
      });


    }
    private void setFavoriteRecyclerview(List<FavoritePost> list) {
        userFavoriteListPostAdapter = new UserFavoriteListPostAdapter(list,requireContext(), this);
        setAdapter.setAdapterRecycleViewHolderLinearlayout(userFavoriteListPostAdapter,recycleViewUserFavoriteFM, LinearLayoutManager.VERTICAL);
    }

    @Override
    public void onItemFavoritePostClick(FavoritePost  post, int position) {
        String tag = "favoriteFragment";
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        ClickitemShowDetail clickitemShowDetail = new ClickitemShowDetail();
        clickitemShowDetail.ClickItemShowDetailInfor(post.getPostId(),R.id.root_favorite_frame,fragmentManager, tag);
    }
}