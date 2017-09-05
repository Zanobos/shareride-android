package com.zano.shareride.network.checkpath;

import com.zano.shareride.network.common.BaseResponse;
import com.zano.shareride.network.common.EnumStatus;

/**
 * Created by a.zanotti on 8/22/2017.
 */

public class CheckPathResponse extends BaseResponse{

    private String requestId;
    private String routeId;
    private EnumStatus status;

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

    public EnumStatus getStatus() {
        return status;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
    }
}
