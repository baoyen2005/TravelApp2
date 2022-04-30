package com.example.travelapp.view.admin;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.function_util.GetPostFromFirebaseStorage;
import com.example.travelapp.function_util.ReplaceFragment;
import com.example.travelapp.function_util.SetAdapter;
import com.example.travelapp.model.Post;
import com.example.travelapp.view.adapter.admin.DisplayAllPostAdminAdapter;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetPostListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class AdminHomeFragment extends BaseFragment implements
        DisplayAllPostAdminAdapter.ClickListenerItemDisplayPost{
    private ImageView imgAddInHomeFmAdmin;
    private GetPostFromFirebaseStorage getPostFromFirebaseStorage;
    private RecyclerView recycleViewListPostInHomeAdmin;
    private DisplayAllPostAdminAdapter displayAllPostAdminAdapter;
    private final List<Post> lisPostInAdmin = new ArrayList<>();
    private final String TAG ="AdminHomeFragment";
    private SetAdapter  setAdapter;
    private FirebaseFirestore db;
    public AdminHomeFragment() {

    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_admin_home;
    }

    @Override
    public void initController() {
        Log.d(TAG, "initController: ");
    }

    @Override
    public void initView(View view) {
        imgAddInHomeFmAdmin = view.findViewById(R.id.imgAddInHomeFmAdmin);
        recycleViewListPostInHomeAdmin = view.findViewById(R.id.recycleViewListPostInHomeAdmin);

    }

    @Override
    public void initData() {
        db = FirebaseFirestore.getInstance();
        getPostFromFirebaseStorage = new GetPostFromFirebaseStorage();

        setAdapter = new SetAdapter(requireActivity());
        displayAllPostAdminAdapter = new DisplayAllPostAdminAdapter(lisPostInAdmin,requireContext(),this);
        setRecycleView();
    }

    private void setRecycleView() {
        getPostFromFirebaseStorage.getAllPostFromFirebase(new InterfaceEventGetPostListener() {
            @Override
            public void getAllPostSuccess(List<Post> postList) {
                displayAllPostAdminAdapter.setListPost(postList);
                setAdapter.setAdapterRecycleViewHolderLinearlayout(
                        displayAllPostAdminAdapter,
                        recycleViewListPostInHomeAdmin,
                        LinearLayoutManager.VERTICAL);
            }

            @Override
            public void getPostsFail(String isFail) {
                Log.d(TAG, "getPostsFail: in setRecycleViewAdminHome");
            }
        });
    }

    @Override
    public void initEvent() {
        setOnClickAddButton();

    }


    private void setOnClickAddButton() {
        imgAddInHomeFmAdmin.setOnClickListener(view -> {
            ReplaceFragment replaceFragmentInit = new ReplaceFragment();
            replaceFragmentInit.replaceFragment(R.id.fragmentRootAdminHome, new AdminAddPostFragment(),requireActivity());
        });
    }

    @Override
    public void onClickShowDetailPost(Post post, int position) {

    }

    @Override
    public void onOptionsMenuClicked(View view, Post post, int position) {
        PopupMenu pm = new PopupMenu(requireContext(), view);
                pm.getMenuInflater().inflate(R.menu.menu_delete_edit_post, pm.getMenu());
                pm.setOnMenuItemClickListener(item -> {
                    Log.d(TAG, "onOptionsMenuClicked: ");
                    switch (item.getItemId()){
                        case R.id.menuItem_delete_post: {
                            Log.d(TAG, "onOptionsMenuClicked: case0");
                            lisPostInAdmin.remove(post);
                            displayAllPostAdminAdapter.setListPost(lisPostInAdmin);
                            deletePostInFirebase(post);
                            setRecycleView();
                            break;
                        }
                        case R.id.menuItem_Edit_post:
                        {
                            Log.d(TAG, "onOptionsMenuClicked: case1");
                            replaceToEditFragment(post);
                            setRecycleView();
                            break;
                        }
                    }
                    return false;
                });
                pm.show();
    }

    private void replaceToEditFragment(Post post) {
        ReplaceFragment replaceFragmentInit = new ReplaceFragment();

        AdminEditPostFragment adminEditPostFragment = AdminEditPostFragment.newInstance(post.getPostId());
        replaceFragmentInit.replaceFragment(R.id.fragmentRootAdminHome,adminEditPostFragment ,requireActivity());


    }

    private void deletePostInFirebase(Post post) {
        db.collection("posts").
                        document(post.getPostId()).
                        delete().
                        addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "Post has been deleted from Database.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Fail to delete the post. ", Toast.LENGTH_SHORT).show();
                            }
                        });
    }
}