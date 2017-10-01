package com.zano.shareride.util.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.zano.shareride.network.common.BoundingBox;
import com.zano.shareride.network.common.GeoPoint;

/**
 * Created by Zano on 01/10/2017, 12:05.
 */

public class ParcelableBoundingBox implements Parcelable {

    private BoundingBox boundingBox;

    public ParcelableBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    protected ParcelableBoundingBox(Parcel in) {
        boundingBox = new BoundingBox();
        ParcelableGeoPoint minimum = in.readParcelable(ParcelableGeoPoint.class.getClassLoader());
        ParcelableGeoPoint maximum = in.readParcelable(ParcelableGeoPoint.class.getClassLoader());
        boundingBox.setMinimum(minimum.getGeoPoint());
        boundingBox.setMaximum(maximum.getGeoPoint());
    }

    public static final Creator<ParcelableBoundingBox> CREATOR = new Creator<ParcelableBoundingBox>() {
        @Override
        public ParcelableBoundingBox createFromParcel(Parcel in) {
            return new ParcelableBoundingBox(in);
        }

        @Override
        public ParcelableBoundingBox[] newArray(int size) {
            return new ParcelableBoundingBox[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        GeoPoint minimum = boundingBox.getMinimum();
        GeoPoint maximum = boundingBox.getMaximum();
        dest.writeParcelable(new ParcelableGeoPoint(minimum),flags);
        dest.writeParcelable(new ParcelableGeoPoint(maximum),flags);
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}
