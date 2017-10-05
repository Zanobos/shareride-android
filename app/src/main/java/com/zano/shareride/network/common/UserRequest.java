package com.zano.shareride.network.common;

import java.util.List;

/**
 * Created by Zano on 10/09/2017, 17:59.
 */

public class UserRequest {

    private String requestId;
    private Location askedPickup;
    private Location proposedPickup;
    private Location askedDelivery;
    private Location proposedDelivery;
    private List<GeoPoint> path;
    private BoundingBox boundingBox;

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

    public Location getAskedDelivery() {
        return askedDelivery;
    }

    public void setAskedDelivery(Location askedDelivery) {
        this.askedDelivery = askedDelivery;
    }

    public Location getProposedDelivery() {
        return proposedDelivery;
    }

    public void setProposedDelivery(Location proposedDelivery) {
        this.proposedDelivery = proposedDelivery;
    }

    public List<GeoPoint> getPath() {
        return path;
    }

    public void setPath(List<GeoPoint> path) {
        this.path = path;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "requestId='" + requestId + '\'' +
                ", askedPickup=" + askedPickup +
                ", proposedPickup=" + proposedPickup +
                ", askedDelivery=" + askedDelivery +
                ", proposedDelivery=" + proposedDelivery +
                ", path=" + path +
                ", boundingBox=" + boundingBox +
                '}';
    }

}
