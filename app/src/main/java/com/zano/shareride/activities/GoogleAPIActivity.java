package com.zano.shareride.activities;

import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by a.zanotti on 7/10/2017.
 */

public abstract class GoogleAPIActivity extends BaseActivity {

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        getFailedConnectionListener() /* OnConnectionFailedListener */)
                .addConnectionCallbacks(getConnectionCallback());

        builder = loadApi(builder);

        mGoogleApiClient = builder.build();

        mGoogleApiClient.connect();
    }

    protected abstract GoogleApiClient.OnConnectionFailedListener getFailedConnectionListener();
    protected abstract GoogleApiClient.ConnectionCallbacks getConnectionCallback();
    protected abstract GoogleApiClient.Builder loadApi(GoogleApiClient.Builder builder);
}
