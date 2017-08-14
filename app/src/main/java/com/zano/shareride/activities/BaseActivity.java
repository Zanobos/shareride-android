package com.zano.shareride.activities;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.zano.shareride.R;
import com.zano.shareride.constants.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zano on 06/06/2017, 17:31.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected static String TAG;

    @Nullable
    @BindView(R.id.my_toolbar)
    protected Toolbar toolbar;

    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = setTag();
        setContentView(layoutId());
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }


    /**
     * Infinite waiting dialog
     *
     * @param message
     */
    protected void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(BaseActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(message);
            progressDialog.show();
        } else {
            Log.d(TAG, "onShowProgressDialog:dialog already shown");
        }
    }

    protected void showProgressDialog(int resourceId) {
        String message = getString(resourceId);
        showProgressDialog(message);
    }


    /**
     * Infinite waiting dialog close
     */
    protected void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        } else {
            Log.d(TAG, "onCloseProgressDialog:dialog not active");
        }
    }

    /**
     * Show toast
     *
     * @param message
     * @param lengthLong
     */
    public void showToast(String message, boolean lengthLong) {
        int length = lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Log.d(TAG, "showToast: message <" + message + ">");
        Toast.makeText(this, message, length).show();
    }

    public void showToast(int resId, boolean lengthLong) {
        int length = lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Log.d(TAG, "showToast: using resource: " + resId);
        Toast.makeText(this, resId, length).show();
    }

    /**
     * If this method is used and request code is != from Constants.RequestCodes.NO_PERMISSION_REQUEST
     * then onRequestPermissionsResult() must be implemented to have a result
     *
     * @param permission  the permission that needs to be checked or retrieved
     * @param requestCode the request code that will be used in the callback
     * @return true if the permission has been already given
     */
    protected boolean checkPermissions(String permission, @Constants.RequestCodes int requestCode) {
        boolean result = false;
        Log.d(TAG, "checkPermissions. Permission:" + permission + ", requestCode: " + requestCode);
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permission)
                == PackageManager.PERMISSION_GRANTED) {
            result = true;
            Log.d(TAG, "checkPermissions. Permission granted");
        } else if (requestCode != Constants.RequestCodes.NO_PERMISSION_REQUEST) {
            Log.d(TAG, "checkPermissions. Permission not granted, asking the user");
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{permission},
                    requestCode);
        }

        return result;
    }

    /**
     * @return the id referencing the layout to use for this activity
     */
    protected abstract int layoutId();

    /**
     * @return the Tag of the activity. In this way at least I am forced to initialize it
     */
    protected abstract String setTag();

}
