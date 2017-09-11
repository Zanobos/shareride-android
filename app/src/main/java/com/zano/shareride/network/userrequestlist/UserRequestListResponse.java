package com.zano.shareride.network.userrequestlist;

import com.zano.shareride.network.common.BaseResponse;
import com.zano.shareride.network.common.UserRequest;

import java.util.Map;

/**
 * Created by Zano on 10/09/2017, 17:58.
 */

public class UserRequestListResponse extends BaseResponse {

    private Map<String,UserRequest> requestMap;

    public Map<String, UserRequest> getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(Map<String, UserRequest> requestMap) {
        this.requestMap = requestMap;
    }

    @Override
    public String toString() {
        return "UserRequestListResponse{" +
                "requestMap=" + requestMap +
                '}';
    }
}
