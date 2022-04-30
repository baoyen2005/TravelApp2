package com.example.travelapp.view.userfragment;

import android.app.SearchManager;
import android.database.Cursor;
import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.controller.user_search_post.UserSearchController;
import com.example.travelapp.function_util.GetPostFromFirebaseStorage;
import com.example.travelapp.function_util.SetAdapter;
import com.example.travelapp.model.Post;
import com.example.travelapp.view.adapter.user.UserSearchListPostAdapter;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentUser extends BaseFragment implements UserSearchListPostAdapter.OnItemPostClickListenerInSearchFragment{

    private SearchManager searchManager;
    private SearchView searchViewInSearchFragment;
    private UserSearchController userSearchController;
    private SearchRecentSuggestions suggestions;
    private UserSearchListPostAdapter userSearchListPostAdapter;
    private RecyclerView recycleViewUserSearchViewFM;
    private List<Post> listPostInSearchFM = new ArrayList<>();
    private final String TAG = "SearchFragmentUser";
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
        userSearchController = new UserSearchController();

    }

    @Override
    public void initView(View view) {
        recycleViewUserSearchViewFM= view.findViewById(R.id.recycleViewUserSearchViewFM);
        searchViewInSearchFragment = view.findViewById(R.id.searchViewInSearchFragment);
        searchViewInSearchFragment.setIconifiedByDefault(false);
        searchViewInSearchFragment.setQueryRefinementEnabled(true);
        searchViewInSearchFragment.requestFocus(1);
    }

    @Override
    public void initData() {
        setAdapter = new SetAdapter(requireActivity());
        userSearchListPostAdapter = new UserSearchListPostAdapter(listPostInSearchFM,requireContext(),this);
//        searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);
//        suggestions = new SearchRecentSuggestions(requireContext(), MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
//        if (searchManager != null) {
//            searchViewInSearchFragment.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
//        }


    }



    @Override
    public void initEvent() {
        initRecycleViewListPost();
        searchViewByKey();
      //  searchViewSuggestion();
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
        searchViewInSearchFragment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(s == null || s.isEmpty()){
                    Log.d(TAG, "onQueryTextSubmit: if null s = " +s);
                    Log.d(TAG, "onQueryTextSubmit: if null listPostInSearchFM =  "+listPostInSearchFM.size());
                    userSearchListPostAdapter.updateData(listPostInSearchFM);
                }
                else{
                    Log.d(TAG, "onQueryTextSubmit: else !=null listPostInSearchFM =  "+listPostInSearchFM.size());

                    Log.d(TAG, "onQueryTextSubmit: else !=null "+ s);
                    userSearchListPostAdapter.getFilter().filter(s);
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
                    Log.d(TAG, "onQueryTextChange: else !=null null favoritePostList =  "+listPostInSearchFM.size());
                    Log.d(TAG, "onQueryTextChange: else !=null "+ s);
                    userSearchListPostAdapter.getFilter().filter(s);
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

    private void searchViewSuggestion() {
        searchViewInSearchFragment.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                CursorAdapter selectedView = searchViewInSearchFragment.getSuggestionsAdapter();
                Cursor cursor = (Cursor) selectedView.getItem(position);
                int index = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
                searchViewInSearchFragment.setQuery(cursor.getString(index), true);
                return true;
            }
        });
    }
    @Override
    public void onItemFavoritePostClick(Post post, int position) {

    }

}