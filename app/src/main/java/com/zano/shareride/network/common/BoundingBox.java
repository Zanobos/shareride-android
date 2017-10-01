package com.zano.shareride.network.common;

/**
 * Created by Zano on 01/10/2017, 11:34.
 */

public class BoundingBox {

    private GeoPoint minimum;
    private GeoPoint maximum;

    public GeoPoint getMinimum() {
        return minimum;
    }

    public void setMinimum(GeoPoint minimum) {
        this.minimum = minimum;
    }

    public GeoPoint getMaximum() {
        return maximum;
    }

    public void setMaximum(GeoPoint maximum) {
        this.maximum = maximum;
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "minimum=" + minimum +
                ", maximum=" + maximum +
                '}';
    }
}
