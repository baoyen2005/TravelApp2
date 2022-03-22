package com.example.travelapp.view.activity.signup;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseActivity;
import com.example.travelapp.controller.createaccount.CreateAccController;
import com.example.travelapp.view.activity.home.MainActivityUser;
import com.example.travelapp.view.activity.login.interface_login.InterfaceLoginView;
import com.example.travelapp.view.activity.login.activity_login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class CreateAccActivity extends BaseActivity implements InterfaceLoginView {
    private ImageView imgChooseImageAvt;
    private Uri filePath;
    private Button  btnCreateAcc;
    private ImageView imgBack;
    private ImageView imgAvatar;
    private final int PICK_IMAGE_REQUEST = 22;
    private EditText edtUserNameCreateAcc, edtUserEmailCreateAcc,
            edtPasswordCreateAcc, edtPassAgainCreateAcc, edtAddressCreateAcc, edtPhoneCreateAcc;
    private CreateAccController createAccController;
    private ProgressDialog loadingBar;
    private TextView tvBackCreateAcc;

    private FirebaseAuth firebaseAuth;

    @Override
    public void setAdjustScreen() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void initBaseController() {
        createAccController = new CreateAccController(this, CreateAccActivity.this);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_create_acc;
    }

    @Override
    public void initview(Bundle savedInstanceState) {
        imgAvatar = findViewById(R.id.imgavt_createAcc);
        imgChooseImageAvt =findViewById(R.id.img_add_avatar_createAcc);
        edtUserNameCreateAcc = findViewById(R.id.edtUserNameCreateAcc);
        edtPhoneCreateAcc = findViewById(R.id.edtUserPhoneCreateAcc);
        edtAddressCreateAcc = findViewById(R.id.edtUserAddressCreateAcc);
        edtPasswordCreateAcc = findViewById(R.id.edtUserPasswordCreateAcc);
        edtPassAgainCreateAcc = findViewById(R.id.edtUserAgainPasswordCreateAcc);
        edtUserEmailCreateAcc = findViewById(R.id.edtUserEmailCreateAcc);
        btnCreateAcc = findViewById(R.id.btnCreateAcc);
        imgBack = findViewById(R.id.iconBackCreateAcc);
        tvBackCreateAcc=  findViewById(R.id.tvBackCreateAcc);
    }

    @Override
    protected void initData() {
        loadingBar = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void initEvent() {
       chooseAvatarImage();

       createAccount();
       backToLoginActivity();
    }

    private void backToLoginActivity() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvBackCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createAccount() {
        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccController.onCreateAcc(
                        edtUserNameCreateAcc.getText().toString(),
                        edtPhoneCreateAcc.getText().toString(),
                        edtAddressCreateAcc.getText().toString(),
                        edtPasswordCreateAcc.getText().toString(),
                        edtPassAgainCreateAcc.getText().toString(),
                        edtUserEmailCreateAcc.getText().toString(),
                        filePath,
                        loadingBar);
            }
        });
    }

    private void chooseAvatarImage() {
        imgChooseImageAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= 23){
                    if(ContextCompat.checkSelfPermission(CreateAccActivity.this, READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                        chooseImageFromLocal();
                    }
                    else{
                        ActivityCompat.requestPermissions(CreateAccActivity.this, new String[]{READ_EXTERNAL_STORAGE},PICK_IMAGE_REQUEST);
                        //  Toast.makeText(this, "Please give permission to choose image! ", Toast.LENGTH_SHORT).show()
                    }
                }
                else {
                    chooseImageFromLocal();
                }
            }
        });
        if (filePath == null) {
            filePath = new Uri.Builder()
                    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                    .authority(CreateAccActivity.this.getResources().getResourcePackageName(R.drawable.useravatar))
                    .appendPath(CreateAccActivity.this.getResources().getResourceTypeName(R.drawable.useravatar))
                    .appendPath(CreateAccActivity.this.getResources().getResourceEntryName(R.drawable.useravatar))
                    .build();

            Log.d("user",filePath.toString());
        }
    }

    private void chooseImageFromLocal() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            filePath = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgAvatar.setImageBitmap(bitmap);
        }
    }

    @Override
    public void OnUserLoginSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CreateAccActivity.this, MainActivityUser.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void OnUserLoginFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnAdminLoginSuccess(String message) {
        Log.d("TAG", "OnAdminLoginSuccess: ");
    }

    @Override
    public void OnAdminLoginFail(String message) {
        Log.d("TAG", "OnAdminLoginFail: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser() !=null){
            Toast.makeText(this, "Account was saved", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(CreateAccActivity.this, MainActivityUser.class);
//            startActivity(intent);
//            finish();
        }
    }
}