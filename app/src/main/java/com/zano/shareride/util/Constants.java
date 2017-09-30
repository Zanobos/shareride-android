package com.zano.shareride.util;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Created by Zano on 06/06/2017, 18:30.
 */

public class Constants {

    @StringDef({
            ParcelArgs.NAMECLASS,
            ParcelArgs.LOCATION,
            ParcelArgs.CAMERA_POSITION,
            ParcelArgs.USER_REQUEST})
    public @interface ParcelArgs {
        String NAMECLASS = "NAMECLASS";
        String LOCATION = "LOCATION";
        String CAMERA_POSITION = "CAMERA_POSITION";
        String USER_REQUEST = "USER_REQUEST";
    }

    @IntDef({
            RequestCodes.NO_PERMISSION_REQUEST,
            RequestCodes.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION,
            RequestCodes.ACTIVITIES_RESOLVE_ERROR,
            RequestCodes.ACTIVITIES_SIGNUP,
            RequestCodes.ACTIVITIES_LOGIN})
    public @interface RequestCodes {
        int NO_PERMISSION_REQUEST = 0;
        int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
        int ACTIVITIES_RESOLVE_ERROR = 1001;
        int ACTIVITIES_SIGNUP = 1002;
        int ACTIVITIES_LOGIN = 1003;
    }

    @IntDef({
            Scale.LATLONG})
    public @interface Scale {
        int LATLONG = 6;
    }

    public @interface ParcelNullValues {
        String NULL_STRING = "NULLVALUE";
        double NULL_COORD = -1.0;
        LocalDate NULL_DATE = LocalDate.parse("1900");
        LocalTime NULL_TIME = LocalTime.parse("00:00:00.000");
    }
}
