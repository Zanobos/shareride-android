package com.zano.shareride.network.common;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Created by Zano on 16/07/2017, 12:01.
 */

public class Location {

    private String locationName;
    private String address;
    private Double lat;
    private Double lon;
    private LocalDate date;
    private LocalTime time;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
