package com.zano.shareride.network.common;

/**
 * Created by Zano on 30/09/2017, 09:25.
 */

public class GeoPoint {

    private Double latitude;
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "GeoPoint{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
