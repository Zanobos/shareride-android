package com.zano.shareride.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zano.shareride.util.Constants;

/**
 * Created by Zano on 11/09/2017, 15:21.
 */

public abstract class UserLoggedActivity extends AuthenticatedActivity {

    protected FirebaseUser user;

    @Override
    protected FirebaseAuth.AuthStateListener initializeFireBaseAuthListener() {
        return new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(UserLoggedActivity.this, LoginActivity.class);
                    startActivityForResult(intent, Constants.RequestCodes.ACTIVITIES_LOGIN);
                } else {
                    userReady();
                }
            }
        };
    }

    protected abstract void userReady();
}
