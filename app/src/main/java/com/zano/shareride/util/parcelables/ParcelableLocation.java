package com.zano.shareride.util.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.zano.shareride.network.common.Location;
import com.zano.shareride.util.Constants;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Created by Zano on 30/09/2017, 11:42.
 */

public class ParcelableLocation implements Parcelable {

    private Location location;

    protected ParcelableLocation(Parcel in) {
        location = new Location();

        String address = in.readString();
        String locationName = in.readString();
        double latitude = in.readDouble();
        double longitude = in.readDouble();
        LocalDate localDate =(LocalDate) in.readSerializable();
        LocalTime localTime = (LocalTime) in.readSerializable();

        location.setAddress(!address.equals(Constants.ParcelNullValues.NULL_STRING) ? address : null );
        location.setLocationName(!locationName.equals(Constants.ParcelNullValues.NULL_STRING) ? locationName : null);
        location.setLat(latitude != Constants.ParcelNullValues.NULL_COORD ? latitude : null);
        location.setLon(longitude != Constants.ParcelNullValues.NULL_COORD ? longitude : null);
        location.setDate(!localDate.isEqual(Constants.ParcelNullValues.NULL_DATE) ? localDate : null);
        location.setTime(!localTime.isEqual(Constants.ParcelNullValues.NULL_TIME) ? localTime : null);
    }

    public ParcelableLocation(Location location) {
        this.location = location;
    }

    public static final Creator<ParcelableLocation> CREATOR = new Creator<ParcelableLocation>() {
        @Override
        public ParcelableLocation createFromParcel(Parcel in) {
            return new ParcelableLocation(in);
        }

        @Override
        public ParcelableLocation[] newArray(int size) {
            return new ParcelableLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String address = location.getAddress() != null ? location.getAddress() : Constants.ParcelNullValues.NULL_STRING;
        String locationName = location.getLocationName() != null ? location.getLocationName() : Constants.ParcelNullValues.NULL_STRING;
        double latitude = location.getLat() != null ? location.getLat() : Constants.ParcelNullValues.NULL_COORD;
        double longitude = location.getLon() != null ? location.getLon() : Constants.ParcelNullValues.NULL_COORD;
        LocalDate localDate = location.getDate() != null ? location.getDate() : Constants.ParcelNullValues.NULL_DATE;
        LocalTime localTime = location.getTime() != null ? location.getTime() : Constants.ParcelNullValues.NULL_TIME;
        dest.writeString(address);
        dest.writeString(locationName);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeSerializable(localDate);
        dest.writeSerializable(localTime) ;
    }

    public Location getLocation() {
        return location;
    }
}
