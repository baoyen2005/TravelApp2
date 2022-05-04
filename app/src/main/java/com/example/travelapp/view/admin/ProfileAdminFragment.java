package com.example.travelapp.view.admin;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


public class ProfileAdminFragment extends BaseFragment {

    private String uid;
    private TextView txtUserNameInUserProfileScreenAdmin, txtUserPasswordInUserProfileAdmin, txtUserEmailInUserProfileScreenAdmin, txtAddressInUserProfileAdmin, txtPhoneInUserProfileAdmin, tvCurrentUserNameInUserProfileScreenAdmin;
    private EditText edtChangeUserNameAdmin, edtchangepasswordAdmin;
    private Button btnSaveInfoWhenEditUserProfileAdmin;
    private Button ConfirmPasswordAdmin, CancelChangePasswordAdmin, btnConfirmUpdateUsernameAdmin, btnCancelUpdateUsernameAdmin;
    private EditText edtchangeaddressAdmin;
    private Button ConfirmAddressAdmin, CancelChangeAddressAdmin;
    private EditText edtchangepphoneAdmin;
    private Button ConfirmPhoneAdmin, CancelChangePhoneAdmin;
    private EditText edtchangeemailAdmin;
    private Button ConfirmEmailAdmin, CancelChangeEmailAdmin;
    private ImageView imgEditUsernameInUserProfileAdmin,
            imgEditPasswordInUserProfileAdmin,
            imgEditAddressInUserProfileAdmin, imgEditPhoneInProfileScreenAdmin, imgEditEmailInUserProfileAdmin, mainavatarAdmin;
    private FirebaseAuth firebaseAuth;
    public ChangeProfileController changeProfileController;
    private User currentUserUpp;
    private final String TAG ="ProfileFragmentAdmin";
    private Uri mainAvatarUri;

    public ProfileAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_profile_admin;
    }

    @Override
    public void initController() {
        changeProfileController = new ChangeProfileController(requireActivity());

    }

    @Override
    public void initView(View view) {
        tvCurrentUserNameInUserProfileScreenAdmin = view.findViewById(R.id.tvCurrentUserNameInUserProfileScreenAdmin);
        txtUserNameInUserProfileScreenAdmin = view.findViewById(R.id.txtUserNameInUserProfileScreenAdmin);
        txtUserPasswordInUserProfileAdmin =view.findViewById(R.id.txtUserPasswordInUserProfileAdmin);
        txtAddressInUserProfileAdmin = view.findViewById(R.id.txtAddressInUserProfileAdmin);
        txtPhoneInUserProfileAdmin = view.findViewById(R.id.txtPhoneInUserProfileAdmin);
        txtUserEmailInUserProfileScreenAdmin = view.findViewById(R.id.txtUserEmailInUserProfileScreenAdmin);


        btnSaveInfoWhenEditUserProfileAdmin = view.findViewById(R.id.btnSaveInfoWhenEditAdminProfile);

        mainavatarAdmin =view.findViewById(R.id.avatar_profile_fragment_admin);
        imgEditPasswordInUserProfileAdmin =view.findViewById(R.id.imgEditPasswordInUserProfileAdmin);
        imgEditAddressInUserProfileAdmin = view.findViewById(R.id.imgEditAddressInUserProfileAdmin);
        imgEditPhoneInProfileScreenAdmin = view.findViewById(R.id.imgEditPhoneInProfileScreenAdmin);
        imgEditEmailInUserProfileAdmin = view.findViewById(R.id.imgEditEmailInUserProfileAdmin);
        imgEditUsernameInUserProfileAdmin = view.findViewById(R.id.imgEditUsernameInUserProfileAdmin);
        btnSaveInfoWhenEditUserProfileAdmin.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initData() {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserUpp = new User();
        getCurrentUser();
    }
    private void getCurrentUser() {
        Log.d(TAG,"get user: "+firebaseAuth.getUid());
        GetUserFromFireStorage getUserFromFireStorage = new GetUserFromFireStorage();
        getUserFromFireStorage.getCurrentUser(firebaseAuth.getUid(), new InterfaceEventGetCurrentUserListener() {
            @Override
            public void getCurrentUserSuccess(User currentUser) {
                currentUserUpp = currentUser;
                Log.d(TAG,"currentAdmin"+currentUser);
                uid=currentUser.getUid();
                tvCurrentUserNameInUserProfileScreenAdmin.setText(currentUser.getUsername());
                txtUserNameInUserProfileScreenAdmin.setText(currentUser.getUsername());
                txtUserPasswordInUserProfileAdmin.setText(currentUser.getPassword());
                txtAddressInUserProfileAdmin.setText(currentUser.getAddress());
                txtPhoneInUserProfileAdmin.setText(currentUser.getPhone());
                txtUserEmailInUserProfileScreenAdmin.setText(currentUser.getEmail());
                if(currentUser.getImageURL()==null){
                    mainavatarAdmin.setImageResource(R.drawable.useravatar);
                }
                else{
                    Glide.with(requireContext())
                            .load(Uri.parse(currentUser.getImageURL()))
                            .into(mainavatarAdmin);
                }
            }

            @Override
            public void getCurrentUserFail(String isFail) {
                Log.d(TAG,"ERROR cant get");
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
        mainavatarAdmin.setOnClickListener(view -> {
            chooseImage();
        });

        imgEditUsernameInUserProfileAdmin.setOnClickListener(v -> {
            btnSaveInfoWhenEditUserProfileAdmin.setVisibility(View.VISIBLE);
            getViewFromDialog(R.layout.dialog_changeusername);
            edtChangeUserNameAdmin =layoutView.findViewById(R.id.edtChangeUserName);
            btnConfirmUpdateUsernameAdmin = layoutView.findViewById(R.id.btnConfirmUpdateUsername);
            btnCancelUpdateUsernameAdmin = layoutView.findViewById(R.id.btnCancelUpdateUsername);
            btnConfirmUpdateUsernameAdmin.setOnClickListener(view -> {
                if (edtChangeUserNameAdmin != null && edtChangeUserNameAdmin.length()>0) {
                    txtUserNameInUserProfileScreenAdmin.setText(edtChangeUserNameAdmin.getText().toString());
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(alertDialog.getContext(), "Please check or enter your username", Toast.LENGTH_SHORT).show();
                }

            });
            btnCancelUpdateUsernameAdmin.setOnClickListener(view -> alertDialog.dismiss());
        });

        imgEditPasswordInUserProfileAdmin.setOnClickListener(v -> {
            btnSaveInfoWhenEditUserProfileAdmin.setVisibility(View.VISIBLE);
            getViewFromDialog(R.layout.dialog_changepassword);
            edtchangepasswordAdmin =layoutView.findViewById(R.id.edtchangepassword);
            ConfirmPasswordAdmin = layoutView.findViewById(R.id.btnConfirmPassword);
            CancelChangePasswordAdmin = layoutView.findViewById(R.id.btnCancelChangePassword);
            ConfirmPasswordAdmin.setOnClickListener(view -> {
                if (edtchangepasswordAdmin != null && edtchangepasswordAdmin.length()>6) {
                    txtUserPasswordInUserProfileAdmin.setText(edtchangepasswordAdmin.getText().toString());
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(alertDialog.getContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                }

            });
            CancelChangePasswordAdmin.setOnClickListener(view -> alertDialog.dismiss());
        });
        imgEditAddressInUserProfileAdmin.setOnClickListener(v -> {
            btnSaveInfoWhenEditUserProfileAdmin.setVisibility(View.VISIBLE);
            getViewFromDialog(R.layout.dialog_changepaddress);
            edtchangeaddressAdmin =layoutView.findViewById(R.id.edtchangeaddress);
            ConfirmAddressAdmin = layoutView.findViewById(R.id.btnConfirmAddress);
            CancelChangeAddressAdmin = layoutView.findViewById(R.id.btnCancelChangeAddress);
            ConfirmAddressAdmin.setOnClickListener(view -> {
                if (edtchangeaddressAdmin !=null && edtchangeaddressAdmin.length()>0) {
                    txtAddressInUserProfileAdmin.setText(edtchangeaddressAdmin.getText().toString());
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(alertDialog.getContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                }
            });
            CancelChangeAddressAdmin.setOnClickListener(view -> alertDialog.dismiss());
        });
        imgEditPhoneInProfileScreenAdmin.setOnClickListener(v -> {
            btnSaveInfoWhenEditUserProfileAdmin.setVisibility(View.VISIBLE);
            getViewFromDialog(R.layout.dialog_changephone);
            edtchangepphoneAdmin =layoutView.findViewById(R.id.edtchangephone);
            String a= txtPhoneInUserProfileAdmin.getText().toString();
            ConfirmPhoneAdmin = layoutView.findViewById(R.id.btnConfirmPhone);
            CancelChangePhoneAdmin = layoutView.findViewById(R.id.btnCancelChangePhone);
            ConfirmPhoneAdmin.setOnClickListener(view -> {
                if (edtchangepphoneAdmin != null && edtchangepphoneAdmin.length()>=10) {
                    txtPhoneInUserProfileAdmin.setText(edtchangepphoneAdmin.getText().toString());
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(alertDialog.getContext(), "Please check or enter your phone", Toast.LENGTH_SHORT).show();
                }
            });
            CancelChangePhoneAdmin.setOnClickListener(view -> alertDialog.dismiss());
        });
        imgEditEmailInUserProfileAdmin.setOnClickListener(v -> {
            btnSaveInfoWhenEditUserProfileAdmin.setVisibility(View.VISIBLE);
            getViewFromDialog(R.layout.dialog_changeemail);
            edtchangeemailAdmin =layoutView.findViewById(R.id.edtchangeemail);
            ConfirmEmailAdmin = layoutView.findViewById(R.id.btnConfirmEmail);
            CancelChangeEmailAdmin = layoutView.findViewById(R.id.btnCancelChangeEmail);
            ConfirmEmailAdmin.setOnClickListener(view -> {
                if (edtchangeemailAdmin !=null && edtchangeemailAdmin.length()>0) {
                    txtUserEmailInUserProfileScreenAdmin.setText(edtchangeemailAdmin.getText().toString());
                    alertDialog.dismiss();
                }
                else {
                    Toast.makeText(alertDialog.getContext(), "Please enter or check your email", Toast.LENGTH_SHORT).show();
                }

            });
            CancelChangeEmailAdmin.setOnClickListener(view -> alertDialog.dismiss());
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
                        mainavatarAdmin.setImageURI(imageUri);
                        mainAvatarUri = imageUri;
                    }
                }
            }
    );


    private void saveProfile() {
        btnSaveInfoWhenEditUserProfileAdmin.setOnClickListener(v -> {

            if(!txtUserEmailInUserProfileScreenAdmin.equals(currentUserUpp.getEmail()) && currentUserUpp.getEmail() != null)
            {
                Log.d(TAG, "saveProfile: "+ currentUserUpp.getPassword()+ "\t"+currentUserUpp.getEmail());
                changeProfileController.updateEmailToAuthen(firebaseAuth.getUid(),
                        txtUserEmailInUserProfileScreenAdmin.getText().toString(),
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
            else if(!txtUserPasswordInUserProfileAdmin.equals(currentUserUpp.getPassword()) && currentUserUpp.getPassword() != null){
                changeProfileController.updatePasswordToAuthen(txtUserPasswordInUserProfileAdmin.getText().toString(),
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
        Log.d("Address",txtAddressInUserProfileAdmin.getText().toString());
        changeProfileController.updateProfile(firebaseAuth.getUid(),
                txtUserNameInUserProfileScreenAdmin.getText().toString(),
                txtUserPasswordInUserProfileAdmin.getText().toString(),
                txtAddressInUserProfileAdmin.getText().toString(),
                txtPhoneInUserProfileAdmin.getText().toString(),
                txtUserEmailInUserProfileScreenAdmin.getText().toString(), mainAvatarUri);
        Toast.makeText(requireContext(), "updateProfile successfully", Toast.LENGTH_SHORT).show();
    }

}