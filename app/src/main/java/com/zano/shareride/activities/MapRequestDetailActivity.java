package com.zano.shareride.activities;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.zano.shareride.R;

/**
 * Created by Zano on 30/09/2017, 10:05.
 */

public class MapRequestDetailActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
