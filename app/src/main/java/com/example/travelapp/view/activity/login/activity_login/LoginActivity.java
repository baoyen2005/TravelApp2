package com.example.travelapp.view.activity.login.activity_login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.travelapp.R;
import com.example.travelapp.base.BaseActivity;
import com.example.travelapp.base.ILog;
import com.example.travelapp.controller.login.LoginController;
import com.example.travelapp.view.activity.home.MainActivityUser;
import com.example.travelapp.view.activity.login.interface_login.IOnLoadInfoListenerLogin;
import com.example.travelapp.view.activity.login.interface_login.IOnLoadUpdateInfoLogin;
import com.example.travelapp.view.activity.login.interface_login.InterfaceLoginView;
import com.example.travelapp.view.activity.signup.CreateAccActivity;
import com.example.travelapp.view.admin.MainActivityAdmin;


public class LoginActivity extends BaseActivity implements InterfaceLoginView {
    private EditText edtUserNameEmailLogin, edtPassword, edtUserEmailRecoverPassword, edtNewPassword;
    private Button btnLogin, btnConfirmUserName, btnCancelDialogResetPass;
    private ProgressDialog loadingBar;
    public LoginController loginController;
    private ILog iLog;
    private TextView createAccountTv, tvLoginByGoogle;
    private TextView tvLoginByFacebook, tvRecoverPassLogin;
    private static final String TAG = "FacebookLogin";

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
        edtUserNameEmailLogin = findViewById(R.id.edtUserNameEmailLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        loadingBar = new ProgressDialog(this);
        createAccountTv = findViewById(R.id.tvCreateAcc_Login);
        tvLoginByFacebook = findViewById(R.id.tvLoginByFacebook);
        tvLoginByGoogle = findViewById(R.id.tvLoginByGmail);
        tvRecoverPassLogin = findViewById(R.id.tvRecoverPass_Login);
    }

    @Override
    protected void initData() {
        iLog = new ILog();

    }

    @Override
    protected void initEvent() {
        buttonLoginCLick();
        createAccountOnClick();
        loginByFacebook();
        loginByGoogle();
        forgotPassword();

    }

    private void forgotPassword() {
        tvRecoverPassLogin.setOnClickListener(view -> {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            View layoutView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
            dialogBuilder.setView(layoutView);
            Dialog alertDialog = dialogBuilder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            alertDialog.show();

            edtUserEmailRecoverPassword = layoutView.findViewById(R.id.edtUserEmailRecoverPassword);
            edtNewPassword = layoutView.findViewById(R.id.edtNewPassword);
            btnConfirmUserName = layoutView.findViewById(R.id.btnConfirmUserName);
            btnCancelDialogResetPass = layoutView.findViewById(R.id.btnCancel_DialogResetPass);

            btnConfirmUserName.setOnClickListener(view1 ->
                    loginController.checkConfirmUserEmail(edtUserEmailRecoverPassword.getText().toString(), new IOnLoadInfoListenerLogin() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(boolean isCheck, String userid) {
                    if (!isCheck || userid == null) {
                        Toast.makeText(LoginActivity.this, "User is wrong or not exits. Please check again", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                    else {

                        edtNewPassword.setVisibility(View.VISIBLE);
                        btnConfirmUserName.setText("Update");
                        if (edtNewPassword != null && edtNewPassword.length()>6) {
                            loginController.updateNewPassword(edtUserEmailRecoverPassword.getText().toString(),
                                    edtNewPassword.getText().toString(), userid,
                                    new IOnLoadUpdateInfoLogin() {
                                        @Override
                                        public void onSuccess(boolean isCheck) {
                                            if (isCheck) {
                                                alertDialog.dismiss();
                                                Toast.makeText(alertDialog.getContext(), "Update password successfully, please login", Toast.LENGTH_SHORT).show();
                                            } else {
                                                alertDialog.dismiss();
                                                Toast.makeText(alertDialog.getContext(), "Update password fail, please try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure() {
                                            alertDialog.dismiss();

                                            Toast.makeText(alertDialog.getContext(), "Update password fail, please try again", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Password is invalid", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                @Override
                public void onFailure() {
                    Toast.makeText(LoginActivity.this, "Confirm failed, please try again!!!", Toast.LENGTH_SHORT).show();
                }

            }));
            btnCancelDialogResetPass.setOnClickListener(view12 -> alertDialog.dismiss());
        });
    }

    private void loginByGoogle() {
        tvLoginByGoogle.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, GoogleAuthLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
    }

    private void loginByFacebook() {
        tvLoginByFacebook.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, FacebookAuthenficationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });

    }


    private void createAccountOnClick() {
        createAccountTv.setOnClickListener(view -> {
            Intent intentAc = new Intent(LoginActivity.this, CreateAccActivity.class);
            startActivity(intentAc);
            finish();

        });
    }


    private void buttonLoginCLick() {
        btnLogin.setOnClickListener(view -> {
            loginController.onLoginByEmail(edtUserNameEmailLogin.getText().toString(), edtPassword.getText().toString(), loadingBar);
        });
    }

    @Override
    public void OnUserLoginSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivityUser.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void OnUserLoginFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnAdminLoginSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivityAdmin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void OnAdminLoginFail(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
