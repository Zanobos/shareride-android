package com.zano.shareride.network.confirmrequest;

import com.zano.shareride.network.common.BaseRequest;

/**
 * Created by Zano on 16/07/2017, 11:59.
 */

public class ConfirmRequestRequest extends BaseRequest {

    private String requestId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "ConfirmRequestRequest{" +
                "requestId='" + requestId + '\'' +
                '}';
    }
}
