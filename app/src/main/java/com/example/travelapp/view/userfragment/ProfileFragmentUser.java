package com.example.travelapp.view.userfragment;

import static com.example.travelapp.constant.UserHomeConstant.TAG_USER_HOME;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.controller.Profile.ChangeProfileController;
import com.example.travelapp.controller.login.LoginController;
import com.example.travelapp.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileFragmentUser extends BaseFragment {
    public LoginController loginController;
    private String uid;
    private TextView usernameprf, passwordprf, emailprf, addressprf,phoneprf,usname;
    private EditText edtchangepassword;
    private Button btnsaveinfo;
    private Button ConfirmPassword,CancelChangePassword;
    private EditText edtchangeaddress;
    private Button ConfirmAddress,CancelChangeAddress;
    private EditText edtchangepphone;
    private Button ConfirmPhone,CancelChangePhone;
    private EditText edtchangeemail;
    private Button ConfirmEmail,CancelChangeEmail;
    private ImageView editpassword,editaddress,editphone, editemail,mainavatar;
    private FirebaseAuth firebaseAuth;
    public ChangeProfileController changeProfileController;
    public ProfileFragmentUser() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_profile_user;
    }

    @Override
    public void initController() {

    }

    @Override
    public void initView(View view) {
        usname = view.findViewById(R.id.tvCurrentUserNameInEditScreen);
        usernameprf = view.findViewById(R.id.txtUserNameInEditScreen);
        passwordprf =view.findViewById(R.id.edtUserPasswordInUserProfile);
        addressprf = view.findViewById(R.id.edtAddressInUserProfile);
        phoneprf = view.findViewById(R.id.edtPhoneInUserProfile);
        emailprf = view.findViewById(R.id.email_prf);


        btnsaveinfo = view.findViewById(R.id.saveInfoWhenEditUserProfile);

        mainavatar=view.findViewById(R.id.avatar_home_fragment);
        editpassword =view.findViewById(R.id.imgEditPasswordInUserProfile);
        editaddress = view.findViewById(R.id.imgEditAddressInUserProfile);
        editphone = view.findViewById(R.id.imgEditPhoneInProfileScreen);
        editemail = view.findViewById(R.id.imgEditEmailInUserProfile);

        btnsaveinfo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initData() {
        firebaseAuth = FirebaseAuth.getInstance();
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

                                User currentUser = setCurrentUser(user);
                                uid=currentUser.getUid();
                                usname.setText(currentUser.getUsername());
                                usernameprf.setText(currentUser.getUsername());
                                passwordprf.setText(currentUser.getPassword());
                                addressprf.setText(currentUser.getAddress());
                                phoneprf.setText(currentUser.getPhone());
                                emailprf.setText(currentUser.getEmail());
                                if(currentUser.getImageURL()==null){
                                    mainavatar.setImageResource(R.drawable.useravatar);
                                }
                                else{
                                    Glide.with(requireContext())
                                            .load(Uri.parse(currentUser.getImageURL()))
                                            .into(mainavatar);
                                }
                                Log.d(TAG_USER_HOME, "onSuccess: current user " + currentUser.getUsername());
                            } else {
                                Log.d(TAG_USER_HOME, "on null: current user null");

                            }
                        }

                    }
                });

    }
    private User setCurrentUser(User user ) {
        User currentUser = new User();
        currentUser.setUid(user.getUid());
        currentUser.setUsername(user.getUsername());
        currentUser.setImageURL(user.getImageURL());
        currentUser.setEmail(user.getEmail());
        currentUser.setAddress(user.getAddress());
        currentUser.setPassword(user.getPassword());
        currentUser.setPhone(user.getPhone());
        return currentUser;
    }

    @Override
    public void initEvent() {
        changeprf();

    }
    public void changeprf(){
        String username=usname.getText().toString();

        editpassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                btnsaveinfo.setVisibility(View.VISIBLE);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
                View layoutView = getLayoutInflater().inflate(R.layout.dialog_changepassword, null);
                dialogBuilder.setView(layoutView);
                Dialog alertDialog = dialogBuilder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                alertDialog.show();

                edtchangepassword=layoutView.findViewById(R.id.edtchangepassword);
                ConfirmPassword = layoutView.findViewById(R.id.btnConfirmPassword);
                CancelChangePassword = layoutView.findViewById(R.id.btnCancelChangePassword);
                ConfirmPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edtchangepassword != null && edtchangepassword.length()>6) {
                            passwordprf.setText(edtchangepassword.getText().toString());
                            alertDialog.dismiss();
                        }
                        else {
                            Toast.makeText(alertDialog.getContext(), "Password khong dung dinh dang", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                CancelChangePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        editaddress.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                btnsaveinfo.setVisibility(View.VISIBLE);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
                View layoutView = getLayoutInflater().inflate(R.layout.dialog_changepaddress, null);
                dialogBuilder.setView(layoutView);
                Dialog alertDialog = dialogBuilder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                alertDialog.show();

                edtchangeaddress=layoutView.findViewById(R.id.edtchangeaddress);
                ConfirmAddress = layoutView.findViewById(R.id.btnConfirmAddress);
                CancelChangeAddress = layoutView.findViewById(R.id.btnCancelChangeAddress);
                ConfirmAddress.setOnClickListener(view -> {
                    if (edtchangeaddress!=null && edtchangeaddress.length()>0) {
                        addressprf.setText(edtchangeaddress.getText().toString());
                        alertDialog.dismiss();
                    }
                    else {
                        Toast.makeText(alertDialog.getContext(), "Vui long nhap ", Toast.LENGTH_SHORT).show();
                    }


                });
                CancelChangeAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        editphone.setOnClickListener(v -> {
            btnsaveinfo.setVisibility(View.VISIBLE);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
            View layoutView = getLayoutInflater().inflate(R.layout.dialog_changephone, null);
            dialogBuilder.setView(layoutView);
            Dialog alertDialog = dialogBuilder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog.show();

            edtchangepphone=layoutView.findViewById(R.id.edtchangephone);
            String a=phoneprf.getText().toString();
            ConfirmPhone = layoutView.findViewById(R.id.btnConfirmPhone);
            CancelChangePhone = layoutView.findViewById(R.id.btnCancelChangePhone);
            ConfirmPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edtchangepphone != null && edtchangepphone.length()>=10) {
                        phoneprf.setText(edtchangepphone.getText().toString());
                        alertDialog.dismiss();
                    }
                    else {
                        Toast.makeText(alertDialog.getContext(), "SDT khong dung dinh dang", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            CancelChangePhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        });
        editemail.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                btnsaveinfo.setVisibility(View.VISIBLE);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
                View layoutView = getLayoutInflater().inflate(R.layout.dialog_changeemail, null);
                dialogBuilder.setView(layoutView);
                Dialog alertDialog = dialogBuilder.create();
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                alertDialog.show();

                edtchangeemail=layoutView.findViewById(R.id.edtchangeemail);
                ConfirmEmail = layoutView.findViewById(R.id.btnConfirmEmail);
                CancelChangeEmail = layoutView.findViewById(R.id.btnCancelChangeEmail);
                ConfirmEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edtchangeemail!=null && edtchangeemail.length()>0) {
                            emailprf.setText(edtchangeemail.getText().toString());
                            alertDialog.dismiss();
                        }
                        else {
                            Toast.makeText(alertDialog.getContext(), "Vui long nhap ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                CancelChangeEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        btnsaveinfo.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                changeProfileController.updateProfile(username,passwordprf.getText().toString(),addressprf.getText().toString(),
                        phoneprf.getText().toString(),emailprf.getText().toString(),uid);
            }
        });
    }
}