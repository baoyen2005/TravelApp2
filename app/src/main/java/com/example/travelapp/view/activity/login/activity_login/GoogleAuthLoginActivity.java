package com.example.travelapp.view.activity.login.activity_login;


import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class GoogleAuthLoginActivity extends LoginActivity {
    private static final int RC_SIGN_IN = 101;
    private static final String TAG = "GoogleLogin";
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.example.travelapp.R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent sigInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(sigInIntent, RC_SIGN_IN);//bi deprecated rồi--okkk
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task  = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle: "+ account.getId());
                loginController.handleGoogleLoginFirebase(account.getIdToken(), mAuth,account);

            } catch (ApiException e) {
                e.printStackTrace();
                Log.d(TAG, "Google sign in failed", e);
            }

        }

    }
}
