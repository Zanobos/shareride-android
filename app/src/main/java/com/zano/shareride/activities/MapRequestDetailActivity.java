package com.zano.shareride.activities;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.zano.shareride.R;
import com.zano.shareride.network.common.BoundingBox;
import com.zano.shareride.network.common.GeoPoint;
import com.zano.shareride.network.common.UserRequest;
import com.zano.shareride.util.Constants;
import com.zano.shareride.util.parcelables.ParcelableUserRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zano on 30/09/2017, 10:05.
 */

public class MapRequestDetailActivity extends BaseActivity implements OnMapReadyCallback {

    private static final int MAP_PADDING = 40;

    private UserRequest userRequest;

    private GoogleMap mMap;
    private Polyline polylinePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        ParcelableUserRequest parcelableUserRequest = bundle.getParcelable(Constants.ParcelArgs.USER_REQUEST);
        userRequest = parcelableUserRequest.getUserRequest();

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

        Iterable<LatLng> path = convertPath(userRequest.getPath());
        PolylineOptions pathOptions = new PolylineOptions().addAll(path);
        polylinePath = mMap.addPolyline(pathOptions);

        LatLngBounds boundingBox = convertBoundingBox(userRequest.getBoundingBox());

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundingBox, MAP_PADDING));
    }

    private Iterable<LatLng> convertPath(List<GeoPoint> path) {
        List<LatLng> coordinates = new ArrayList<>();
        for(GeoPoint geoPoint : path){
            LatLng latLng = convertGeoPoint(geoPoint);
            coordinates.add(latLng);
        }
        return coordinates;
    }

    private LatLngBounds convertBoundingBox(BoundingBox boundingBox) {
        LatLng minimum = convertGeoPoint(boundingBox.getMinimum());
        LatLng maximum = convertGeoPoint(boundingBox.getMaximum());
        return new LatLngBounds(minimum,maximum);
    }

    private LatLng convertGeoPoint(GeoPoint geoPoint) {
        return new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude());
    }
}
