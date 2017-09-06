package com.zano.shareride.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zano.shareride.R;
import com.zano.shareride.fragments.RouteDetailsFragment;
import com.zano.shareride.network.NetworkController;
import com.zano.shareride.network.checkpath.CheckPathRequest;
import com.zano.shareride.network.checkpath.CheckPathResponse;
import com.zano.shareride.network.common.AdditionalInfo;
import com.zano.shareride.network.common.EnumStatus;
import com.zano.shareride.network.common.UserInfo;
import com.zano.shareride.network.confirmrequest.ConfirmRequestRequest;
import com.zano.shareride.network.confirmrequest.ConfirmRequestResponse;
import com.zano.shareride.util.Constants;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.math.BigDecimal;

import butterknife.BindView;

public class MapActivity extends GoogleAPIActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        RouteDetailsFragment.RouteDetailsFragmentListener, LocationListener {

    private static final int DEFAULT_ZOOM = 15;
    private static final int INTERVAL_UPDATE = 10;
    private static final int FASTEST_INTERVAL_UPDATE = 1;
    private static final int SECOND_IN_MILLISECONDS = 1000;
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
    private String requestId;
    private String routeId;

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
        requestId = null;
        routeId = null;
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

                RouteDetailsFragment routeDetailsFragment = new RouteDetailsFragment();
                routeDetailsFragment.show(getSupportFragmentManager(), "RouteDetailsFragment");
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmRequestRequest confirmRequest = new ConfirmRequestRequest();
                confirmRequest.setRequestId(requestId);
                confirmRequest.setRouteId(routeId);
                showProgressDialog(R.string.dialog_confirming);
                NetworkController.getInstance(MapActivity.this).addConfirmRequestRequest(confirmRequest, new Response.Listener<ConfirmRequestResponse>() {
                    @Override
                    public void onResponse(ConfirmRequestResponse response) {
                        closeProgressDialog();
                        routeChecked = false;
                        requestId = null;
                        routeId = null;
                        enableButtons();
                        showToast(R.string.toast_path_confirmed, false);
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgressDialog();
                        Log.e(TAG, "onConfirmPathResponse:" + error.getMessage(), error);
                        showToast(R.string.toast_error, false);
                    }
                });
            }
        });

    }

    private CheckPathRequest createCheckPathRequest(boolean deliveryTime, int numberOfSeats, LocalDate date, LocalTime time, String name, String userId) {
        CheckPathRequest checkPathRequest = new CheckPathRequest();

        AdditionalInfo additionalInfo = new AdditionalInfo();
        additionalInfo.setNeedAssistance(false);
        additionalInfo.setNumberOfSeats(numberOfSeats);
        checkPathRequest.setAdditionalInfo(additionalInfo);

        com.zano.shareride.network.common.Location pickup = new com.zano.shareride.network.common.Location();
        pickup.setLon(BigDecimal.valueOf(markerStart.getPosition().longitude).setScale(Constants.Scale.LATLONG, BigDecimal.ROUND_HALF_UP).doubleValue());
        pickup.setLat(BigDecimal.valueOf(markerStart.getPosition().latitude).setScale(Constants.Scale.LATLONG, BigDecimal.ROUND_HALF_UP).doubleValue());
        if (markerStart.getTag() != null) {
            Place place = (Place) markerStart.getTag();
            pickup.setAddress(place.getAddress().toString());
            pickup.setLocationName(place.getName().toString());
        }
        checkPathRequest.setPickup(pickup);

        com.zano.shareride.network.common.Location delivery = new com.zano.shareride.network.common.Location();
        delivery.setLon(BigDecimal.valueOf(markerFinish.getPosition().longitude).setScale(Constants.Scale.LATLONG, BigDecimal.ROUND_HALF_UP).doubleValue());
        delivery.setLat(BigDecimal.valueOf(markerFinish.getPosition().latitude).setScale(Constants.Scale.LATLONG, BigDecimal.ROUND_HALF_UP).doubleValue());
        if (markerFinish.getTag() != null) {
            Place place = (Place) markerFinish.getTag();
            delivery.setAddress(place.getAddress().toString());
            delivery.setLocationName(place.getName().toString());
        }
        checkPathRequest.setDelivery(delivery);

        if (deliveryTime) {
            delivery.setDate(date);
            delivery.setTime(time);
        } else {
            pickup.setDate(date);
            pickup.setTime(time);
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setName(name);
        userInfo.setUserId(userId);
        checkPathRequest.setUserInfo(userInfo);

        return checkPathRequest;
    }

    private void enableButtons() {
        if (markerStart != null && markerFinish != null) {
            checkButton.setEnabled(true);
        } else {
            checkButton.setEnabled(false);
        }
        if (routeChecked && requestId != null) {
            confirmButton.setEnabled(true);
        } else {
            confirmButton.setEnabled(false);
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
            if (place != null) {
                marker.setTag(place);
            }
        
            if (markerStart == null) {
                markerStart = marker;
            } else /*if (markerFinish == null)*/ {
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
            Log.d(TAG, "Last know location is null. Trying to get an update.");
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            boolean gpsEnabled;
            try {
                gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {
                Log.e(TAG, "Error in checking availability of GPS",ex);
                gpsEnabled = false;
            }

            if(gpsEnabled && mLocationPermissionGranted) {
                Log.d(TAG, "GPS is enabled");
                LocationRequest mLocationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(INTERVAL_UPDATE * SECOND_IN_MILLISECONDS)
                        .setFastestInterval(FASTEST_INTERVAL_UPDATE * SECOND_IN_MILLISECONDS);
                showProgressDialog("Locating...");
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                Log.d(TAG, "GPS is not enabled and we have no clue about our whereabouts. Using a default");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case Constants.RequestCodes.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
                break;
            }
            default:
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        closeProgressDialog();

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, Constants.RequestCodes.ACTIVITIES_RESOLVE_ERROR);
                //Thrown if Google Play services canceled the original PendingIntent
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                Log.e(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                        + connectionResult.getErrorCode(), e);
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                    + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RequestCodes.ACTIVITIES_RESOLVE_ERROR) {
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
        else if (requestCode == Constants.RequestCodes.ACTIVITIES_LOGIN) {
            if (resultCode == RESULT_OK) {
                showToast(R.string.toast_login_ok, false);
            }
        }
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

    @Override
    public void onDialogPositiveClick(boolean deliveryTime, int numberOfSeats, LocalDate date, LocalTime time) {
        Log.d(TAG, "onDialogPositiveClick");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name;
        String userId;
        if(user != null) {
            name = user.getDisplayName();
            userId = user.getUid();
        } else {
            Log.d(TAG, "onDialogPositiveClick:not auth");
            Intent intent = new Intent(MapActivity.this, LoginActivity.class);
            startActivityForResult(intent, Constants.RequestCodes.ACTIVITIES_LOGIN);
            return;
        }

        CheckPathRequest checkPathRequest = createCheckPathRequest(deliveryTime, numberOfSeats, date, time, name, userId);
        showProgressDialog(R.string.dialog_checking);
        NetworkController.getInstance(MapActivity.this).addCheckPathRequest(checkPathRequest, new Response.Listener<CheckPathResponse>() {
            @Override
            public void onResponse(CheckPathResponse response) {
                closeProgressDialog();
                if(response.getStatus().equals(EnumStatus.ACCEPTED)){
                    //TODO use the path that came back response
                    routeChecked = true;
                    requestId = response.getRequestId();
                    routeId = response.getRouteId();
                    markerStart.remove();
                    markerStart = null;
                    markerFinish.remove();
                    markerFinish = null;
                    showToast(getString(R.string.dialog_path_available), false);
                } else {

                    showToast(getString(R.string.dialog_path_not_available), false);
                }
                enableButtons();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeProgressDialog();
                Log.e(TAG, "onCheckPathResponse:" + error.getMessage(), error);
                showToast(R.string.toast_error, false);
            }
        });
    }

    @Override
    public void onDialogNegativeClick() {
        Log.d(TAG, "onDialogPositiveClick");
    }

    @Override
    public void onLocationChanged(Location location) {
        if(mLastKnownLocation == null) {
            mLastKnownLocation = location;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
            //After one single update, I disconnect the listener
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            closeProgressDialog();
        }
    }
}
