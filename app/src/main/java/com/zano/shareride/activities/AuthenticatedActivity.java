package com.zano.shareride.activities;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by a.zanotti on 8/14/2017.
 */

public abstract class AuthenticatedActivity extends BaseActivity {

    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;

    protected abstract FirebaseAuth.AuthStateListener initializeFireBaseAuthListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = initializeFireBaseAuthListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    protected void signOut() {
        mAuth.signOut();
    }
}
