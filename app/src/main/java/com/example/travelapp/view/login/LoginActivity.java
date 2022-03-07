package com.example.travelapp.view.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseActivity;
import com.example.travelapp.base.ILog;
import com.example.travelapp.controller.login.FacebookAuthenficationActivity;
import com.example.travelapp.controller.login.LoginController;
import com.example.travelapp.model.User;
import com.example.travelapp.view.home.HomeActivity;
import com.example.travelapp.view.signup.CreateAccActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class LoginActivity extends BaseActivity implements InterfaceLoginView {
    private EditText edtUserName, edtPassword;
    private Button btnLogin;
    private ProgressDialog loadingBar;
    public LoginController loginController;
    private ILog iLog;
    private TextView createAccountTv, tvLoginByGoogle;
    private LoginButton btnLoginByFacebook;
    private TextView tvLoginByFacebook;
    private static final String TAG = "FacebookLogin";
    private static final int RC_SIGN_IN = 12345;
    public LoginActivity() {
        super();
    }

    @Override
    public void setAdjustScreen() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void initBaseController() {
        loginController = new LoginController(this, LoginActivity.this);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_login;
    }

    @Override
    public void initview(Bundle savedInstanceState) {
        edtUserName = findViewById(R.id.edtUserNameLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        loadingBar = new ProgressDialog(this);
        createAccountTv = findViewById(R.id.tvCreateAcc_Login);
        tvLoginByFacebook = findViewById(R.id.tvLoginByFacebook);
        tvLoginByGoogle = findViewById(R.id.tvLoginByGmail);
    }

    @Override
    protected void initData() {
        iLog = new ILog();

    }

    @Override
    protected void initEvent() {
        buttonLoginCLick();
        tvCreateAccountOnClick();
        tvLoginByFacebook();
        tvLoginByGoogle();

    }

    private void tvLoginByGoogle() {
        tvLoginByGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, GoogleAuthLoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void tvLoginByFacebook() {
        tvLoginByFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FacebookAuthenficationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

    }





    private void tvCreateAccountOnClick() {
        createAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAc = new Intent(LoginActivity.this, CreateAccActivity.class);
                startActivity(intentAc);
                finish();

            }
        });
    }


    private void buttonLoginCLick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginController.onLoginByUsername(edtUserName.getText().toString(), edtPassword.getText().toString(), loadingBar);
                iLog.setTag("login");
                iLog.setMes("success");
                iLog.log();
            }
        });
    }

    @Override
    public void OnLoginSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void OnLoginError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
