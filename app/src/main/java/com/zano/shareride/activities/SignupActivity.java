package com.zano.shareride.activities;

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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.zano.shareride.R;

import butterknife.BindView;

/**
 * Created by a.zanotti on 8/14/2017.
 */

public class SignupActivity extends AuthenticatedActivity {

    @BindView(R.id.activity_signup_input_email) protected EditText emailET;
    @BindView(R.id.activity_signup_input_password) protected EditText passwordET;
    @BindView(R.id.activity_signup_input_name) protected EditText nameET;
    @BindView(R.id.activity_signup_btn_signup) protected Button signupBtn;
    @BindView(R.id.activity_signup_tv_login) protected TextView loginTV;

    private String mEmail;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                if (validate(name, email, password)) {
                    signUp(name, email, password);
                } else {
                    onSignupFailed();
                }
            }
        });

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public boolean validate(String name, String email, String password) {
        boolean valid = true;

        if (name.isEmpty() || name.length() < 3) {
            nameET.setError("at least 3 characters");
            valid = false;
        } else {
            nameET.setError(null);
        }

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

    public void signUp(String name, String email, String password) {

        this.mName = name;
        this.mEmail = email;

        signupBtn.setEnabled(false);
        showProgressDialog(R.string.dialog_authenticating);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        signupBtn.setEnabled(true);
                        closeProgressDialog();
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "createUserWithEmail:failed", task.getException());
                            showToast(R.string.activity_signup_registration_failed, false);
                        }
                    }
                });
    }

    public void onSignupFailed() {
        showToast(R.string.toast_complete_form, true);
    }

    @Override
    protected FirebaseAuth.AuthStateListener initializeFireBaseAuthListener() {
        return new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(mName)
                            .build();
                    user.updateProfile(profileUpdates);

                    user.updateEmail(mEmail);

                    setResult(RESULT_OK, null);
                    finish();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_signup;
    }

    @Override
    protected String setTag() {
        return "SignupActivity";
    }
}
