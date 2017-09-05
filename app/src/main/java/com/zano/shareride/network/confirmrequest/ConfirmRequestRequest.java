package com.zano.shareride.network.confirmrequest;

import com.zano.shareride.network.common.BaseRequest;

/**
 * Created by Zano on 16/07/2017, 11:59.
 */

public class ConfirmRequestRequest extends BaseRequest {

    private String requestId;
    private String routeId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    @Override
    public String toString() {
        return "ConfirmRequestRequest{" +
                "requestId='" + requestId + '\'' +
                ", routeId='" + routeId + '\'' +
                '}';
    }
}
