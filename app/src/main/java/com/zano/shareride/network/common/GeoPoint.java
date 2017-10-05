package com.zano.shareride.network.common;

/**
 * Created by Zano on 30/09/2017, 09:25.
 */

public class GeoPoint {

    private Integer position;
    private Double latitude;
    private Double longitude;
    private EnumRouteLocationType type;
    private boolean userPath;

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

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

    public EnumRouteLocationType getType() {
        return type;
    }

    public void setType(EnumRouteLocationType type) {
        this.type = type;
    }

    public boolean isUserPath() {
        return userPath;
    }

    public void setUserPath(boolean userPath) {
        this.userPath = userPath;
    }

    @Override
    public String toString() {
        return "GeoPoint{" +
                "position=" + position +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", type=" + type +
                ", userPath=" + userPath +
                '}';
    }

}
