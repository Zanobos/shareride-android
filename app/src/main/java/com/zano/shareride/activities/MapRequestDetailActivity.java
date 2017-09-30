package com.zano.shareride.activities;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.zano.shareride.R;
import com.zano.shareride.network.common.UserRequest;
import com.zano.shareride.util.Constants;
import com.zano.shareride.util.parcelables.ParcelableUserRequest;

/**
 * Created by Zano on 30/09/2017, 10:05.
 */

public class MapRequestDetailActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UserRequest userRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        userRequest = ((ParcelableUserRequest) bundle.getParcelable(Constants.ParcelArgs.USER_REQUEST)).getUserRequest();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected int layoutId() {
        return R.layout.activity_map_request_detail;
    }

    @Override
    protected String setTag() {
        return "MapRequestDetailActivity";
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
