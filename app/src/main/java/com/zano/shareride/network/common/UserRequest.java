package com.zano.shareride.network.common;

import java.util.List;

/**
 * Created by Zano on 10/09/2017, 17:59.
 */

public class UserRequest {

    private String requestId;
    private Location askedPickup;
    private Location proposedPickup;
    private Location askedDevilery;
    private Location proposedDevilery;
    private List<GeoPoint> path;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Location getAskedPickup() {
        return askedPickup;
    }

    public void setAskedPickup(Location askedPickup) {
        this.askedPickup = askedPickup;
    }

    public Location getProposedPickup() {
        return proposedPickup;
    }

    public void setProposedPickup(Location proposedPickup) {
        this.proposedPickup = proposedPickup;
    }

    public Location getAskedDevilery() {
        return askedDevilery;
    }

    public void setAskedDevilery(Location askedDevilery) {
        this.askedDevilery = askedDevilery;
    }

    public Location getProposedDevilery() {
        return proposedDevilery;
    }

    public void setProposedDevilery(Location proposedDevilery) {
        this.proposedDevilery = proposedDevilery;
    }

    public List<GeoPoint> getPath() {
        return path;
    }

    public void setPath(List<GeoPoint> path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "requestId='" + requestId + '\'' +
                ", askedPickup=" + askedPickup +
                ", proposedPickup=" + proposedPickup +
                ", askedDevilery=" + askedDevilery +
                ", proposedDevilery=" + proposedDevilery +
                '}';
    }
}
