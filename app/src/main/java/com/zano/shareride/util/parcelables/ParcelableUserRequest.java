package com.zano.shareride.util.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

import com.zano.shareride.network.common.UserRequest;

/**
 * Created by Zano on 30/09/2017, 11:37.
 */

public class ParcelableUserRequest implements Parcelable {

    private UserRequest userRequest;

    public ParcelableUserRequest(UserRequest userRequest) {
        this.userRequest = userRequest;
    }

    protected ParcelableUserRequest(Parcel in) {
        userRequest = new UserRequest();
        userRequest.setAskedDelivery(((ParcelableLocation)in.readParcelable(ParcelableLocation.class.getClassLoader())).getLocation());
        userRequest.setAskedPickup(((ParcelableLocation)in.readParcelable(ParcelableLocation.class.getClassLoader())).getLocation());
        userRequest.setProposedDelivery(((ParcelableLocation)in.readParcelable(ParcelableLocation.class.getClassLoader())).getLocation());
        userRequest.setProposedPickup(((ParcelableLocation)in.readParcelable(ParcelableLocation.class.getClassLoader())).getLocation());
        userRequest.setPath(ParcelableGeoPoint.convertParcelableList(in.createTypedArrayList(ParcelableGeoPoint.CREATOR)));
        userRequest.setBoundingBox(((ParcelableBoundingBox)in.readParcelable(ParcelableBoundingBox.class.getClassLoader())).getBoundingBox());
    }

    public static final Creator<ParcelableUserRequest> CREATOR = new Creator<ParcelableUserRequest>() {
        @Override
        public ParcelableUserRequest createFromParcel(Parcel in) {
            return new ParcelableUserRequest(in);
        }

        @Override
        public ParcelableUserRequest[] newArray(int size) {
            return new ParcelableUserRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(new ParcelableLocation(userRequest.getAskedDelivery()),flags);
        dest.writeParcelable(new ParcelableLocation(userRequest.getAskedPickup()),flags);
        dest.writeParcelable(new ParcelableLocation(userRequest.getProposedDelivery()),flags);
        dest.writeParcelable(new ParcelableLocation(userRequest.getProposedPickup()),flags);
        dest.writeTypedList(ParcelableGeoPoint.convertList(userRequest.getPath()));
        dest.writeParcelable(new ParcelableBoundingBox(userRequest.getBoundingBox()),flags);
    }

    public UserRequest getUserRequest() {
        return userRequest;
    }
}
