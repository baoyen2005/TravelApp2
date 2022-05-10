package com.example.travelapp.view.userfragment;

import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.controller.user_search_post.UserSearchController;
import com.example.travelapp.function_util.ClickitemShowDetail;
import com.example.travelapp.function_util.GetPostFromFirebaseStorage;
import com.example.travelapp.function_util.SetAdapter;
import com.example.travelapp.model.Post;
import com.example.travelapp.view.adapter.user.UserSearchListPostAdapter;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SearchFragmentUser extends BaseFragment implements
        UserSearchListPostAdapter.OnItemPostClickListenerInSearchFragment {
    private UserSearchListPostAdapter userSearchListPostAdapter;
    private SearchView searchViewInSearchFragment;;
    private SetAdapter setAdapter;
    private RecyclerView recycleViewUserSearchViewFM;
    private List<Post> listPostInSearchFM;
    private UserSearchController userSearchController;
    private final String TAG = "SearchFragmentUser";
    private String uid;
    public SearchFragmentUser() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_search_user;
    }

    @Override
    public void initController() {
        userSearchController = new UserSearchController();

    }
    
    @Override
    public void initView(View view) {

        recycleViewUserSearchViewFM = view.findViewById(R.id.recycleViewUserSearchViewFM);
        searchViewInSearchFragment =view.findViewById(R.id.searchViewInUserSearch);

    }



    @Override
    public void initData() {
        listPostInSearchFM = new ArrayList<>();
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
            searchViewInSearchFragment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    if(s == null || s.isEmpty()){
                        Log.d(TAG, "onQueryTextSubmit: if null s = " +s);
                        Log.d(TAG, "onQueryTextSubmit: if null favoritePostList =  "+listPostInSearchFM.size());
                        userSearchListPostAdapter.updateData(listPostInSearchFM);
                    }
                    else{
                       Log.d(TAG, "onQueryTextSubmit: else !=null "+ s);
                        userSearchController.filter(s,listPostInSearchFM,userSearchListPostAdapter);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if(s == null || s.isEmpty()){
                        Log.d(TAG, "onQueryTextChange: if s null "+s);
                        Log.d(TAG, "onQueryTextChange: if null favoritePostList =  "+listPostInSearchFM.size());
                        userSearchListPostAdapter.updateData(listPostInSearchFM);
                    }
                    else{
                        Log.d(TAG, "onQueryTextChange: else !=null "+ s);
                        userSearchController.filter(s,listPostInSearchFM,userSearchListPostAdapter);
                    }
                    return false;
                }
            });

            searchViewInSearchFragment.setOnCloseListener(() -> {
                searchViewInSearchFragment.setIconified(false);
                searchViewInSearchFragment.setVisibility( View.INVISIBLE);
                userSearchListPostAdapter.updateData(listPostInSearchFM);
                Log.d(TAG, "searchViewByName:close favoritePostList.size()"+ listPostInSearchFM.size());
                return false;
            });

    }
    private void setPostListForRecyclerview() {
        GetPostFromFirebaseStorage getPostFromFirebaseStorage = new GetPostFromFirebaseStorage();
        getPostFromFirebaseStorage.getAllPostFromFirebase(new InterfaceEventGetPostListener() {
            @Override
            public void getAllPostSuccess(List<Post> postList) {
                listPostInSearchFM.clear();
                setAllPostRecycleview(postList);
                listPostInSearchFM.addAll(postList);
            }

            @Override
            public void getPostsFail(String isFail) {
                Log.d(TAG, "getPostsFail: ");
            }
        });


    }
    private void setAllPostRecycleview(List<Post> list) {
        userSearchListPostAdapter = new UserSearchListPostAdapter(list,requireContext(), this);
        setAdapter.setAdapterRecycleViewHolderLinearlayout(userSearchListPostAdapter,recycleViewUserSearchViewFM, LinearLayoutManager.VERTICAL);
    }


    @Override
    public void onItemPostInSearchClick(Post post, int position) {

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        ClickitemShowDetail clickitemShowDetail = new ClickitemShowDetail();
        clickitemShowDetail.ClickItemShowDetailInfor(post.getPostId(),R.id.root_search_fragment,fragmentManager, TAG);
    }
}