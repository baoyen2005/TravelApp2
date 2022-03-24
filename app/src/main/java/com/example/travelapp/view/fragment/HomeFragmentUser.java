package com.example.travelapp.view.fragment;

import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME;

import android.net.Uri;
import android.util.Log;
import android.widget.SearchView;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.constant.UserHomeConstant;
import com.example.travelapp.controller.homefragment.HomeFMController;
import com.example.travelapp.model.User;
import com.example.travelapp.view.interfacefragment.InterfaceHomeFmView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class HomeFragmentUser extends BaseFragment implements InterfaceHomeFmView {
    private ImageView avatarHomeFragment, imgNotificationHomeFm;
    private TextView tvGoodMorningHomeFm, tvUserNameHomeFm, tvWelcomeHomeFr, tvSeeMoreHomeFm;
    private TextView tvDiscoverHomeFM;
    private SearchView searchViewHomeFr;
    private RecyclerView recycleViewCategories, recycleViewRecommended, recycleViewKnowYourWorld;
    private HomeFMController homeFMController;
    private FirebaseAuth firebaseAuth;
    private User currentUser;
    private UserHomeConstant userHomeConstant;

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
        currentUser = new User();
        userHomeConstant = new UserHomeConstant();
        getCurrentUser();
    }

    private void getCurrentUser() {
        FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("uid", firebaseAuth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<User> listUser = (ArrayList<User>) queryDocumentSnapshots.toObjects(User.class);
                        for (int i = 0; i < listUser.size(); i++) {
                            if (listUser.get(i) != null) {
                                User user = listUser.get(i);
                                currentUser.setUid(user.getUid());
                                currentUser.setUsername(user.getUsername());
                                currentUser.setFilePath(user.getFilePath());
                                currentUser.setEmail(user.getEmail());
                                currentUser.setAddress(user.getAddress());
                                currentUser.setPassword(user.getPassword());
                                currentUser.setPhone(user.getPhone());
                                Log.d(TAG_USER_HOME, "onSuccess: current user " + currentUser.getUsername());
                            } else {
                                Log.d(TAG_USER_HOME, "on null: current user null");

                            }
                        }

                    }
                });

    }

    private void setTextForWelcome() {
        String text = "Khám phá một\nthế giới mới.";
        tvWelcomeHomeFr.setText(text);
    }

    @Override
    public void initEvent() {
        setTextForWelcome();
        if (currentUser != null) {
            setUserAvatar(currentUser);
            setTextForTVGoodmorning_Name(currentUser);
        }
    }

    private void setTextForTVGoodmorning_Name(User currentUser) {
        Date currentTime = Calendar.getInstance().getTime();
        if (6 <= currentTime.getHours() && currentTime.getHours() < 12) {
            tvGoodMorningHomeFm.setText("Good morning");
        } else if (12 <= currentTime.getHours() && currentTime.getHours() < 18) {
            tvGoodMorningHomeFm.setText("Good afternoon");
        } else if (18 <= currentTime.getHours() && currentTime.getHours() < 24) {
            tvGoodMorningHomeFm.setText("Good evening");
        } else {
            tvGoodMorningHomeFm.setText("Good night");
        }
        tvUserNameHomeFm.setText(currentUser.getUsername());
    }

    private void setUserAvatar(User currentUser) {
        avatarHomeFragment.setImageURI(Uri.parse(currentUser.getFilePath()));
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
}