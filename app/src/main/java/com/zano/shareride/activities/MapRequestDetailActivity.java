package com.zano.shareride.activities;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.internal.ty;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.zano.shareride.R;
import com.zano.shareride.network.common.BoundingBox;
import com.zano.shareride.network.common.EnumRouteLocationType;
import com.zano.shareride.network.common.GeoPoint;
import com.zano.shareride.network.common.UserRequest;
import com.zano.shareride.util.Constants;
import com.zano.shareride.util.parcelables.ParcelableUserRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zano on 30/09/2017, 10:05.
 */

public class MapRequestDetailActivity extends BaseActivity implements OnMapReadyCallback {

    private static final int MAP_PADDING = 40;
    private static final float LINE_WIDTH_NO_PATH = 5;
    private static final int PATTERN_GAP_LENGTH_PX  = 20;
    private static final int LINE_COLOR_PATH  = Color.RED;

    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);

    private UserRequest userRequest;

    private GoogleMap mMap;

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

        Iterable<LatLng> pathStart = convertPath(userRequest.getPath(), EnumRouteLocationType.START);
        PolylineOptions pathStartOptions = new PolylineOptions().addAll(pathStart).width(LINE_WIDTH_NO_PATH);
        mMap.addPolyline(pathStartOptions);

        Iterable<LatLng> pathUser = convertPath(userRequest.getPath(), EnumRouteLocationType.PICKUPSHIPMENT);
        PolylineOptions pathUserOptions = new PolylineOptions().addAll(pathUser).color(LINE_COLOR_PATH);
        mMap.addPolyline(pathUserOptions);

        Iterable<LatLng> pathEnd = convertPath(userRequest.getPath(), EnumRouteLocationType.END);
        PolylineOptions pathEndOptions = new PolylineOptions().addAll(pathEnd).width(LINE_WIDTH_NO_PATH);
        mMap.addPolyline(pathEndOptions);


        LatLngBounds boundingBox = convertBoundingBox(userRequest.getBoundingBox());

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundingBox, MAP_PADDING));
    }

    private Iterable<LatLng> convertPath(List<GeoPoint> path, EnumRouteLocationType type) {
        List<LatLng> coordinates = new ArrayList<>();
        if(type == null) {
            //All the path
            for(GeoPoint geoPoint : path){
                LatLng latLng = convertGeoPoint(geoPoint);
                coordinates.add(latLng);
            }
        } else if (type == EnumRouteLocationType.START) {
            //From START to PICKUP
            boolean start = true;
            for(GeoPoint geoPoint : path){
                if(!geoPoint.isUserPath() && start) {
                    LatLng latLng = convertGeoPoint(geoPoint);
                    coordinates.add(latLng);
                }
                if(geoPoint.getType() == EnumRouteLocationType.PICKUPSHIPMENT) {
                    start = false;
                }
            }
        } else if (type == EnumRouteLocationType.PICKUPSHIPMENT) {
            //From PICKUP to DELIVER
            for(GeoPoint geoPoint : path){
                if(geoPoint.isUserPath()) {
                    LatLng latLng = convertGeoPoint(geoPoint);
                    coordinates.add(latLng);
                }
            }
        }else if (type == EnumRouteLocationType.END) {
            //From DELIVER to END
            boolean end = false;
            for(GeoPoint geoPoint : path){
                if(!geoPoint.isUserPath() && end) {
                    LatLng latLng = convertGeoPoint(geoPoint);
                    coordinates.add(latLng);
                }
                if(geoPoint.getType() == EnumRouteLocationType.DELIVERSHIPMENT) {
                    end = true;
                }
            }
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
