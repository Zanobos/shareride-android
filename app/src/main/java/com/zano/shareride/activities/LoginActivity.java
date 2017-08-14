package com.zano.shareride.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zano.shareride.R;
import com.zano.shareride.constants.Constants;

import butterknife.BindView;

/**
 * Created by a.zanotti on 8/14/2017.
 */

public class LoginActivity extends AuthenticatedActivity {

    @BindView(R.id.activity_login_input_email) protected EditText emailET;
    @BindView(R.id.activity_login_input_password) protected EditText passwordET;
    @BindView(R.id.activity_login_btn_login) protected Button loginBtn;
    @BindView(R.id.activity_login_tv_signup) protected TextView signupTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                if(validate(email,password)) {
                    signIn(email,password);
                } else {
                    onLoginFailed();
                }
            }
        });

        signupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, Constants.RequestCodes.ACTIVITIES_SIGNUP);
            }
        });
    }

    private boolean validate(String email,String password) {
        boolean valid = true;

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("enter a valid email address");
            valid = false;
        } else {
            emailET.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordET.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordET.setError(null);
        }

        return valid;
    }

    protected void signIn(String email, String password){

        loginBtn.setEnabled(false);
        showProgressDialog(R.string.dialog_logging_in);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        loginBtn.setEnabled(true);
                        closeProgressDialog();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            showToast(R.string.activity_login_auth_failed,false);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @Override
    protected FirebaseAuth.AuthStateListener initializeFireBaseAuthListener() {
        return new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    setResult(RESULT_OK);
                    finish();
                } else {
                    // User is signed out, nothing to do
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    public void onLoginFailed() {
        showToast(R.string.toast_complete_form,true);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected String setTag() {
        return "LoginActivity";
    }
}
