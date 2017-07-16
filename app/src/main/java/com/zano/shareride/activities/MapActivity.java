package com.zano.shareride.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zano.shareride.R;
import com.zano.shareride.constants.Constants;
import com.zano.shareride.network.NetworkController;
import com.zano.shareride.network.checkpath.CheckPathRequest;
import com.zano.shareride.network.common.AdditionalInfo;
import com.zano.shareride.network.common.UserInfo;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;

import butterknife.BindView;

public class MapActivity extends GoogleAPIActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(45.116177, 7.74261);

    private GoogleMap mMap;

    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;
    private CameraPosition mCameraPosition;
    private MarkerOptions markerOptStart;
    private MarkerOptions markerOptFinish;

    private Marker markerStart;
    private Marker markerFinish;

    private boolean routeChecked;

    @BindView(R.id.check_button) protected Button checkButton;
    @BindView(R.id.confirm_button) protected Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProgressDialog("Authenticating...");
        mLocationPermissionGranted = false;
        markerOptStart = new MarkerOptions().title("Start").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        markerOptFinish = new MarkerOptions().title("Finish").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
        markerStart = null;
        markerFinish = null;
        routeChecked = false;
    }

    @Override
    protected GoogleApiClient.OnConnectionFailedListener getFailedConnectionListener() {
        return this;
    }

    @Override
    protected GoogleApiClient.ConnectionCallbacks getConnectionCallback() {
        return this;
    }

    @Override
    protected GoogleApiClient.Builder loadApi(GoogleApiClient.Builder builder) {
        return builder
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API);
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected String setTag() {
        return "MapActivity";
    }


    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(Constants.ParcelArgs.CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(Constants.ParcelArgs.LOCATION, mLastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCameraPosition = savedInstanceState.getParcelable(Constants.ParcelArgs.CAMERA_POSITION);
        mLastKnownLocation = savedInstanceState.getParcelable(Constants.ParcelArgs.LOCATION);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        updateLocationUI();

        getDeviceLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng newLatLon) {
                putMarkerOnMap(newLatLon, null);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marker.getTitle();
                if (markerStart != null && title.equals(markerStart.getTitle())) {
                    markerStart.remove();
                    markerStart = null;
                } else if (markerFinish != null && title.equals(markerFinish.getTitle())) {
                    markerFinish.remove();
                    markerFinish = null;
                }

                enableButtons();

                return true;
            }
        });

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // Get info about the selected place and move the camera
                if (mMap == null) {
                    return;
                }

                boolean set = putMarkerOnMap(place.getLatLng(), place);
                if (set) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), DEFAULT_ZOOM));
                }

            }

            @Override
            public void onError(Status status) {
                showToast("failure", true);
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckPathRequest checkPathRequest = createCheckPathRequest(false,new Date());
                showProgressDialog("Checking...");
                NetworkController.getInstance(MapActivity.this).addCheckPathRequest(checkPathRequest, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        closeProgressDialog();
                        //TODO check response
                        showToast("Path available!",false);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgressDialog();
                        Log.e(TAG, "onErrorResponse:" + error.getMessage(),error);
                        showToast("An error occured!",false);
                    }
                });
            }
        });

    }

    private CheckPathRequest createCheckPathRequest(boolean deliveryTime, Date date) {
        CheckPathRequest checkPathRequest = new CheckPathRequest();

        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setNeedAssistance(false);
        additionalInfo.setNumberOfSeats(1);
        checkPathRequest.setAdditionalInfo(additionalInfo);

        com.zano.shareride.network.common.Location pickup = new com.zano.shareride.network.common.Location();
        pickup.setLon(BigDecimal.valueOf(markerStart.getPosition().longitude).setScale(Constants.Scale.LATLONG, BigDecimal.ROUND_HALF_UP).doubleValue());
        pickup.setLat(BigDecimal.valueOf(markerStart.getPosition().latitude).setScale(Constants.Scale.LATLONG, BigDecimal.ROUND_HALF_UP).doubleValue());
        if(markerStart.getTag()!=null){
            Place place = (Place) markerStart.getTag();
            pickup.setAddress(place.getAddress().toString());
            pickup.setLocationName(place.getName().toString());
        }
        checkPathRequest.setPickup(pickup);

        com.zano.shareride.network.common.Location delivery = new com.zano.shareride.network.common.Location();
        delivery.setLon(BigDecimal.valueOf(markerFinish.getPosition().longitude).setScale(Constants.Scale.LATLONG, BigDecimal.ROUND_HALF_UP).doubleValue());
        delivery.setLat(BigDecimal.valueOf(markerFinish.getPosition().latitude).setScale(Constants.Scale.LATLONG, BigDecimal.ROUND_HALF_UP).doubleValue());
        if(markerFinish.getTag()!=null){
            Place place = (Place) markerFinish.getTag();
            delivery.setAddress(place.getAddress().toString());
            delivery.setLocationName(place.getName().toString());
        }
        checkPathRequest.setDelivery(delivery);

        if(deliveryTime) {
            delivery.setTime(date.getTime());
        } else {
            pickup.setTime(date.getTime());
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setName("Andrea Zanotti"); //TODO
        userInfo.setUserId("ZANO"); //TODO
        checkPathRequest.setUserInfo(userInfo);

        return checkPathRequest;
    }

    private void enableButtons() {
        if (markerStart != null && markerFinish != null) {
            checkButton.setEnabled(true);
        } else {
            checkButton.setEnabled(false);
        }
        if(routeChecked) {
            confirmButton.setEnabled(true);
        }
    }

    private boolean putMarkerOnMap(LatLng newLatLon, Place place) {
        boolean set = false;
        MarkerOptions markerOptions = null;
        if (markerStart == null) {
            markerOptions = markerOptStart;
        } else if (markerFinish == null) {
            markerOptions = markerOptFinish;
        }

        if (markerOptions != null) {
            Marker marker = mMap.addMarker(markerOptions.position(newLatLon));
            marker.showInfoWindow();
            if(place != null) {
                marker.setTag(place);
            }

            if (markerStart == null) {
                markerStart = marker;
            } else if (markerFinish == null) {
                markerFinish = marker;
            }
            set = true;
        } else {
            showToast(R.string.toast_map_warning, false);
        }

        enableButtons();

        return set;
    }

    @SuppressWarnings("MissingPermission")
    private void updateLocationUI() {

        if (mMap == null) {
            return;
        }

        mLocationPermissionGranted = checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Constants.RequestCodes.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        } else {
            mMap.setMyLocationEnabled(false);
            mLastKnownLocation = null;
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getDeviceLocation() {

        mLocationPermissionGranted = checkPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Constants.RequestCodes.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case Constants.RequestCodes.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
        closeProgressDialog();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Build the map.
        Log.d(TAG, "Play services connected");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        closeProgressDialog();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Play services connection suspended");
    }
}
