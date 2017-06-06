package com.zano.shareride.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.zano.shareride.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zano on 06/06/2017, 17:31.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    @BindView(R.id.my_toolbar) protected Toolbar toolbar;

    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }


    protected void showProgressDialog(String message) {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(BaseActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(message);
            progressDialog.show();
        } else {
            Log.d(TAG, "onShowProgressDialog:dialog already shown");
        }
    }

    public void closeProgressDialog() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        } else {
            Log.d(TAG, "onCloseProgressDialog:dialog not active");
        }
    }

    /**
     * @return the id referencing the layout to use for this activity
     */
    protected abstract int layoutId();

}
