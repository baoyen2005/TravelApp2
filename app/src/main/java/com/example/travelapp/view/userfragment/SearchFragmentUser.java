package com.example.travelapp.view.userfragment;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentUser extends BaseFragment implements UserSearchListPostAdapter.OnItemPostClickListenerInSearchFragment{

    private SearchView searchViewInSearchFragment;
    private UserSearchListPostAdapter userSearchListPostAdapter;
    private RecyclerView recycleViewUserSearchViewFM;
    private final List<Post> listPostInSearchFM = new ArrayList<>();
    private final List<Post> listPostInSearchCopy = new ArrayList<>();
    private final String TAG = "SearchFragmentUser";
    private ImageView imgSearchInUserSearch,imgCloseInUserSearch;
    private EditText edtSearchInSearchFM;

    private DocumentSnapshot lastVisible;
    private boolean isLastItemReached;
    private boolean isLoading;

    List<String> listName;
    private SetAdapter setAdapter;
    public SearchFragmentUser() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_search_user;
    }

    @Override
    public void initController() {
        UserSearchController userSearchController = new UserSearchController();

    }

    @Override
    public void initView(View view) {
        recycleViewUserSearchViewFM= view.findViewById(R.id.recycleViewUserSearchViewFM);
        imgSearchInUserSearch = view.findViewById(R.id.imgSearchInUserSearch);
        imgCloseInUserSearch = view.findViewById(R.id.imgCloseInUserSearch);
        edtSearchInSearchFM= view.findViewById(R.id.edtSearchInSearchFM);
    }

    @Override
    public void initData() {


        setAdapter = new SetAdapter(requireActivity());
        userSearchListPostAdapter = new UserSearchListPostAdapter(listPostInSearchFM,requireContext(),this);
//nut search dau

    }



    @Override
    public void initEvent() {
        initRecycleViewListPost();
        searchViewByKey();
    }
    private void initRecycleViewListPost() {



        GetPostFromFirebaseStorage getPostFromFirebaseStorage = new GetPostFromFirebaseStorage();
        getPostFromFirebaseStorage.getAllPostFromFirebase(new InterfaceEventGetPostListener() {
            @Override
            public void getAllPostSuccess(List<Post> postList) {
                listPostInSearchFM.clear();
                listPostInSearchFM.addAll(postList);
                Log.d(TAG, "getAllPostSuccess: in SearchFragmentUser "+ listPostInSearchFM.size());
                userSearchListPostAdapter.updateData(postList);
                setAdapter.setAdapterRecycleViewHolderLinearlayout(userSearchListPostAdapter,recycleViewUserSearchViewFM, LinearLayoutManager.VERTICAL);
            }

            @Override
            public void getPostsFail(String isFail) {
                Log.d(TAG, "getPostsFail: in SearchFragmentUser ");
            }
        });
    }

    private void searchViewByKey() {
        imgSearchInUserSearch.setOnClickListener(view -> {
            lastVisible=null;
            isLastItemReached=false;
            List<String> listName = new ArrayList<>();
            String key = edtSearchInSearchFM.getText().toString().trim();
            listName.add(key);
            listName.add(key.toLowerCase());
            listName.add(key.toUpperCase());
            if (!key.isEmpty() && listName.size() > 0) {

//                    progress_circular.setVisibility(View.VISIBLE);

                recycleViewUserSearchViewFM.setVisibility(View.GONE);
                loadPosts(false, listName);
            }
        });


    }

    private void loadPosts(boolean isLoadMore, List<String> listName) {
        Query query = FirebaseFirestore.getInstance().collection("posts")
                .whereIn("touristName", listName)
                .limit(10);


        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    if (getActivity().isDestroyed()||getActivity().isFinishing()) {
                        return;
                    }

                    List<Post> list = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        Post post = documentSnapshot.toObject(Post.class);
                        list.add(post);
                    }
                    userSearchListPostAdapter.updateData(list);
                    imgSearchInUserSearch.setVisibility(View.VISIBLE);
                    recycleViewUserSearchViewFM.setVisibility(View.VISIBLE);
               //     progress_circular.setVisibility(View.GONE);
                    int size = querySnapshot.size();
                    if (size > 0) {
                        lastVisible = querySnapshot.getDocuments().get(querySnapshot.size() - 1);
                    }
                    if (size < 10) {
                        isLastItemReached = true;
                    }
                })
                .addOnFailureListener(e -> {
                    if (getActivity().isDestroyed()||getActivity().isFinishing()) {
                        return;
                    }
                    imgSearchInUserSearch.setVisibility(View.VISIBLE);
                    isLoading = false;
                    Log.d("__index", "loadPosts: "+e.getMessage().toString());

                });

    }

    @Override
    public void onItemFavoritePostClick(Post post, int position) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        ClickitemShowDetail clickitemShowDetail = new ClickitemShowDetail();
        clickitemShowDetail.ClickItemShowDetailInfor(post.getPostId(),R.id.root_search_fragment,fragmentManager, TAG);
    }

}