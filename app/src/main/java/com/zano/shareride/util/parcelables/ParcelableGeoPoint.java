package com.zano.shareride.util.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.zano.shareride.network.common.GeoPoint;
import com.zano.shareride.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zano on 30/09/2017, 11:42.
 */

public class ParcelableGeoPoint implements Parcelable {

    private GeoPoint geoPoint;

    public ParcelableGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    protected ParcelableGeoPoint(Parcel in) {
        geoPoint = new GeoPoint();
        double latitude = in.readDouble();
        double longitude = in.readDouble();
        geoPoint.setLatitude(latitude != Constants.ParcelNullValues.NULL_COORD ? latitude : null);
        geoPoint.setLongitude(longitude != Constants.ParcelNullValues.NULL_COORD ? longitude : null);
    }

    public static final Creator<ParcelableGeoPoint> CREATOR = new Creator<ParcelableGeoPoint>() {
        @Override
        public ParcelableGeoPoint createFromParcel(Parcel in) {
            return new ParcelableGeoPoint(in);
        }

        @Override
        public ParcelableGeoPoint[] newArray(int size) {
            return new ParcelableGeoPoint[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        double latitude = geoPoint.getLatitude() != null ? geoPoint.getLatitude() : Constants.ParcelNullValues.NULL_COORD;
        double longitude = geoPoint.getLongitude() != null ? geoPoint.getLongitude() : Constants.ParcelNullValues.NULL_COORD;
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    public static ArrayList<ParcelableGeoPoint> convertList(List<GeoPoint> geoPointList) {
        ArrayList<ParcelableGeoPoint> parcelableGeoPointList = new ArrayList<>();
        for(GeoPoint geoPoint : geoPointList){
            parcelableGeoPointList.add(new ParcelableGeoPoint(geoPoint));
        }

        return parcelableGeoPointList;
    }

    public static ArrayList<GeoPoint> convertParcelableList(List<ParcelableGeoPoint> parcelableGeoPointList) {
        ArrayList<GeoPoint> geoPoints = new ArrayList<>();
        for(ParcelableGeoPoint parcelableGeoPoint : parcelableGeoPointList){
            geoPoints.add(parcelableGeoPoint.getGeoPoint());
        }

        return geoPoints;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }
}
