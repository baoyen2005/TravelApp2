package com.example.travelapp.view.userfragment;

import android.app.Dialog;
import android.content.Intent;
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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.travelapp.R;
import com.example.travelapp.base.BaseFragment;
import com.example.travelapp.controller.Profile.ChangeProfileController;
import com.example.travelapp.controller.Profile.UpdateEmailToFirebase;
import com.example.travelapp.controller.Profile.UpdatePasswordToFirebase;
import com.example.travelapp.function_util.GetUserFromFireStorage;
import com.example.travelapp.model.User;
import com.example.travelapp.view.interfacefragment.InterfaceEventGetCurrentUserListener;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragmentUser extends BaseFragment {
    private String uid;
    private TextView txtUserNameInUserProfileScreen, txtUserPasswordInUserProfile, txtUserEmailInUserProfileScreen, txtAddressInUserProfile, txtPhoneInUserProfile, tvCurrentUserNameInUserProfileScreen;
    private EditText edtChangeUserName,edtchangepassword;
    private Button btnSaveInfoWhenEditUserProfile;
    private Button ConfirmPassword,CancelChangePassword,btnConfirmUpdateUsername, btnCancelUpdateUsername;
    private EditText edtchangeaddress;
    private Button ConfirmAddress,CancelChangeAddress;
    private EditText edtchangepphone;
    private Button ConfirmPhone,CancelChangePhone;
    private EditText edtchangeemail;
    private Button ConfirmEmail,CancelChangeEmail;
    private ImageView imgEditUsernameInUserProfile,
            imgEditPasswordInUserProfile,
            imgEditAddressInUserProfile, imgEditPhoneInProfileScreen, imgEditEmailInUserProfile,mainavatar;
    private FirebaseAuth firebaseAuth;
    public ChangeProfileController changeProfileController;
    private User currentUserUpp;
    private final String TAG ="ProfileFragmentUser";
    private Uri mainAvatarUri;
    public ProfileFragmentUser() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_profile_user;
    }

    @Override
    public void initController() {
        changeProfileController = new ChangeProfileController(requireActivity());

    }

    @Override
    public void initView(View view) {
        tvCurrentUserNameInUserProfileScreen = view.findViewById(R.id.tvCurrentUserNameInUserProfileScreen);
        txtUserNameInUserProfileScreen = view.findViewById(R.id.txtUserNameInUserProfileScreen);
        txtUserPasswordInUserProfile =view.findViewById(R.id.txtUserPasswordInUserProfile);
        txtAddressInUserProfile = view.findViewById(R.id.txtAddressInUserProfile);
        txtPhoneInUserProfile = view.findViewById(R.id.txtPhoneInUserProfile);
        txtUserEmailInUserProfileScreen = view.findViewById(R.id.txtUserEmailInUserProfileScreen);


        btnSaveInfoWhenEditUserProfile = view.findViewById(R.id.btnSaveInfoWhenEditUserProfile);

        mainavatar=view.findViewById(R.id.avatar_profile_fragment);
        imgEditPasswordInUserProfile =view.findViewById(R.id.imgEditPasswordInUserProfile);
        imgEditAddressInUserProfile = view.findViewById(R.id.imgEditAddressInUserProfile);
        imgEditPhoneInProfileScreen = view.findViewById(R.id.imgEditPhoneInProfileScreen);
        imgEditEmailInUserProfile = view.findViewById(R.id.imgEditEmailInUserProfile);
        imgEditUsernameInUserProfile = view.findViewById(R.id.imgEditUsernameInUserProfile);
        btnSaveInfoWhenEditUserProfile.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initData() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserUpp = new User();
        getCurrentUser();
    }
    private void getCurrentUser() {
        GetUserFromFireStorage getUserFromFireStorage = new GetUserFromFireStorage();
        getUserFromFireStorage.getCurrentUser(firebaseAuth.getUid(), new InterfaceEventGetCurrentUserListener() {
            @Override
            public void getCurrentUserSuccess(User currentUser) {
                currentUserUpp = currentUser;
                uid=currentUser.getUid();
                tvCurrentUserNameInUserProfileScreen.setText(currentUser.getUsername());
                txtUserNameInUserProfileScreen.setText(currentUser.getUsername());
                txtUserPasswordInUserProfile.setText(currentUser.getPassword());
                txtAddressInUserProfile.setText(currentUser.getAddress());
                txtPhoneInUserProfile.setText(currentUser.getPhone());
                txtUserEmailInUserProfileScreen.setText(currentUser.getEmail());
                if(currentUser.getImageURL()==null){
                    mainavatar.setImageResource(R.drawable.useravatar);
                }
                else{
                    Glide.with(requireContext())
                            .load(Uri.parse(currentUser.getImageURL()))
                            .into(mainavatar);
                }
            }

            @Override
            public void getCurrentUserFail(String isFail) {

            }
        });


    }

    @Override
    public void initEvent() {
        changeProfile();

    }
    private View layoutView;
    private Dialog alertDialog;
    private void getViewFromDialog(int IDLayout){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireActivity());
        layoutView = getLayoutInflater().inflate(IDLayout, null);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
        //return layoutView;
    }
    public void changeProfile(){
        mainavatar.setOnClickListener(view -> {
            chooseImage();
        });

        imgEditUsernameInUserProfile.setOnClickListener(v -> {
            btnSaveInfoWhenEditUserProfile.setVisibility(View.VISIBLE);
            getViewFromDialog(R.layout.dialog_changeusername);
            edtChangeUserName=layoutView.findViewById(R.id.edtChangeUserName);
            btnConfirmUpdateUsername = layoutView.findViewById(R.id.btnConfirmUpdateUsername);
            btnCancelUpdateUsername = layoutView.findViewById(R.id.btnCancelUpdateUsername);
            btnConfirmUpdateUsername.setOnClickListener(view -> {
                if (edtChangeUserName != null && edtChangeUserName.length()>0) {
                    txtUserNameInUserProfileScreen.setText(edtChangeUserName.getText().toString());
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(alertDialog.getContext(), "Please check or enter your username", Toast.LENGTH_SHORT).show();
                }

            });
            btnCancelUpdateUsername.setOnClickListener(view -> alertDialog.dismiss());
        });

        imgEditPasswordInUserProfile.setOnClickListener(v -> {
            btnSaveInfoWhenEditUserProfile.setVisibility(View.VISIBLE);
            getViewFromDialog(R.layout.dialog_changepassword);
            edtchangepassword=layoutView.findViewById(R.id.edtchangepassword);
            ConfirmPassword = layoutView.findViewById(R.id.btnConfirmPassword);
            CancelChangePassword = layoutView.findViewById(R.id.btnCancelChangePassword);
            ConfirmPassword.setOnClickListener(view -> {
                if (edtchangepassword != null && edtchangepassword.length()>6) {
                    txtUserPasswordInUserProfile.setText(edtchangepassword.getText().toString());
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(alertDialog.getContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                }

            });
            CancelChangePassword.setOnClickListener(view -> alertDialog.dismiss());
        });
        imgEditAddressInUserProfile.setOnClickListener(v -> {
            btnSaveInfoWhenEditUserProfile.setVisibility(View.VISIBLE);
            getViewFromDialog(R.layout.dialog_changepaddress);
            edtchangeaddress=layoutView.findViewById(R.id.edtchangeaddress);
            ConfirmAddress = layoutView.findViewById(R.id.btnConfirmAddress);
            CancelChangeAddress = layoutView.findViewById(R.id.btnCancelChangeAddress);
            ConfirmAddress.setOnClickListener(view -> {
                if (edtchangeaddress!=null && edtchangeaddress.length()>0) {
                    txtAddressInUserProfile.setText(edtchangeaddress.getText().toString());
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(alertDialog.getContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                }
            });
            CancelChangeAddress.setOnClickListener(view -> alertDialog.dismiss());
        });
        imgEditPhoneInProfileScreen.setOnClickListener(v -> {
            btnSaveInfoWhenEditUserProfile.setVisibility(View.VISIBLE);
            getViewFromDialog(R.layout.dialog_changephone);
            edtchangepphone=layoutView.findViewById(R.id.edtchangephone);
            String a= txtPhoneInUserProfile.getText().toString();
            ConfirmPhone = layoutView.findViewById(R.id.btnConfirmPhone);
            CancelChangePhone = layoutView.findViewById(R.id.btnCancelChangePhone);
            ConfirmPhone.setOnClickListener(view -> {
                if (edtchangepphone != null && edtchangepphone.length()>=10) {
                    txtPhoneInUserProfile.setText(edtchangepphone.getText().toString());
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(alertDialog.getContext(), "Please check or enter your phone", Toast.LENGTH_SHORT).show();
                }
            });
            CancelChangePhone.setOnClickListener(view -> alertDialog.dismiss());
        });
        imgEditEmailInUserProfile.setOnClickListener(v -> {
            btnSaveInfoWhenEditUserProfile.setVisibility(View.VISIBLE);
            getViewFromDialog(R.layout.dialog_changeemail);
            edtchangeemail=layoutView.findViewById(R.id.edtchangeemail);
            ConfirmEmail = layoutView.findViewById(R.id.btnConfirmEmail);
            CancelChangeEmail = layoutView.findViewById(R.id.btnCancelChangeEmail);
            ConfirmEmail.setOnClickListener(view -> {
                if (edtchangeemail!=null && edtchangeemail.length()>0) {
                    txtUserEmailInUserProfileScreen.setText(edtchangeemail.getText().toString());
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(alertDialog.getContext(), "Please enter or check your email", Toast.LENGTH_SHORT).show();
                }

            });
            CancelChangeEmail.setOnClickListener(view -> alertDialog.dismiss());
        });
        saveProfile();
    }

    private void chooseImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        imagePickerActivityResult.launch(galleryIntent);
    }

    ActivityResultLauncher<Intent> imagePickerActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result != null) {
                    Uri imageUri = result.getData().getData();
                    Log.d("___Yenlb", "imagePickerActivityResult: imageUri " + imageUri);
                    if (imageUri == null) {
                        Toast.makeText(requireContext(), "Please choose image", Toast.LENGTH_SHORT).show();
                    } else {
                        mainavatar.setImageURI(imageUri);
                        mainAvatarUri = imageUri;
                    }
                }
            }
    );


    private void saveProfile() {
        btnSaveInfoWhenEditUserProfile.setOnClickListener(v -> {

            if(!txtUserEmailInUserProfileScreen.equals(currentUserUpp.getEmail()))
            {
                Log.d(TAG, "saveProfile: "+ currentUserUpp.getPassword()+ "\t"+currentUserUpp.getEmail());
                changeProfileController.updateEmailToAuthen(firebaseAuth.getUid(),
                        txtUserEmailInUserProfileScreen.getText().toString(),
                        currentUserUpp.getEmail(),
                        currentUserUpp.getPassword(),
                        new UpdateEmailToFirebase() {
                            @Override
                            public void updateEmailSuccess() {
                                updateProfile();
                            }

                            @Override
                            public void updatePasswordFailed() {
                                Log.d(TAG, "updateEmailAndPasswordFailed: ");
                                Toast.makeText(requireContext(), "updateEmailFailed", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
            else if(!txtUserPasswordInUserProfile.equals(currentUserUpp.getPassword())){
                changeProfileController.updatePasswordToAuthen(txtUserPasswordInUserProfile.getText().toString(),
                        currentUserUpp.getEmail(),
                        new UpdatePasswordToFirebase() {
                            @Override
                            public void updatePasswordSuccess() {
                               updateProfile();

                            }

                            @Override
                            public void updatePasswordFailed() {
                                Log.d(TAG, "updatePasswordFailed: ");
                                Toast.makeText(requireContext(), "updatePasswordFailed", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
            else{
                updateProfile();
            }
        });
    }

    private void updateProfile() {
        changeProfileController.updateProfile(firebaseAuth.getUid(),
                txtUserNameInUserProfileScreen.getText().toString(),
                txtUserPasswordInUserProfile.getText().toString(),
                txtAddressInUserProfile.getText().toString(),
                txtPhoneInUserProfile.getText().toString(),
                txtUserEmailInUserProfileScreen.getText().toString(), mainAvatarUri);
        Toast.makeText(requireContext(), "updateProfile successfully", Toast.LENGTH_SHORT).show();
    }

}