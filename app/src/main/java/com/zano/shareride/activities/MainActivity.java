package com.zano.shareride.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zano.shareride.R;
import com.zano.shareride.adapters.BaseRecyclerAdapter;
import com.zano.shareride.adapters.ExampleAdapter;
import com.zano.shareride.constants.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends AuthenticatedActivity {

    private BaseRecyclerAdapter adapter;

    @BindView(R.id.my_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.my_button) Button button;
    @BindView(R.id.signout_button) Button signoutBtn;

    @Override
    protected FirebaseAuth.AuthStateListener initializeFireBaseAuthListener() {
        return new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, Constants.RequestCodes.ACTIVITIES_LOGIN);
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String, String> map = new LinkedHashMap<>();
        adapter = new ExampleAdapter(this, R.layout.listitem_example, map);
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected String setTag() {
        return "MainActivity";
    }


}
