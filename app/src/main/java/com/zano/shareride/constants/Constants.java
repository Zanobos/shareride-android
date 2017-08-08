package com.zano.shareride.constants;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

/**
 * Created by Zano on 06/06/2017, 18:30.
 */

public class Constants {

    @StringDef({ParcelArgs.NAMECLASS})
    public @interface ParcelArgs {
        String NAMECLASS = "NAMECLASS";
        String LOCATION = "LOCATION";
        String CAMERA_POSITION = "CAMERA_POSITION";
    }

    @IntDef({
            RequestCodes.NO_PERMISSION_REQUEST,
            RequestCodes.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION})
    public @interface RequestCodes {
        int NO_PERMISSION_REQUEST = 0;
        int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
        int ACTIVITIES_RESOLVE_ERROR = 1001;
    }

    @IntDef({
            Scale.LATLONG})
    public @interface Scale {
        int LATLONG = 6;
    }
}
