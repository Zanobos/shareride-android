package com.zano.shareride.network.confirmrequest;

import com.zano.shareride.network.common.BaseResponse;
import com.zano.shareride.network.common.EnumStatus;

/**
 * Created by a.zanotti on 8/22/2017.
 */

public class ConfirmRequestResponse extends BaseResponse{

    private String requestId;
    private EnumStatus status;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public EnumStatus getStatus() {
        return status;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ConfirmRequestResponse{" +
                "requestId='" + requestId + '\'' +
                ", status=" + status +
                '}';
    }
}
