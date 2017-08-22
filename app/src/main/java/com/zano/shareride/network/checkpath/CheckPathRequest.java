package com.zano.shareride.network.checkpath;

import com.zano.shareride.network.common.AdditionalInfo;
import com.zano.shareride.network.common.BaseRequest;
import com.zano.shareride.network.common.Location;
import com.zano.shareride.network.common.UserInfo;

/**
 * Created by Zano on 16/07/2017, 11:59.
 */

public class CheckPathRequest extends BaseRequest{

    private UserInfo userInfo;
    private Location pickup;
    private Location delivery;
    private AdditionalInfo additionalInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Location getPickup() {
        return pickup;
    }

    public void setPickup(Location pickup) {
        this.pickup = pickup;
    }

    public Location getDelivery() {
        return delivery;
    }

    public void setDelivery(Location delivery) {
        this.delivery = delivery;
    }

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String toString() {
        return "CheckPathRequest{" +
                "userInfo=" + userInfo +
                ", pickup=" + pickup +
                ", delivery=" + delivery +
                ", additionalInfo=" + additionalInfo +
                '}';
    }
}
