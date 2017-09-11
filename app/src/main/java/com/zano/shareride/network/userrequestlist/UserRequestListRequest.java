package com.zano.shareride.network.userrequestlist;

import com.zano.shareride.network.common.BaseRequest;
import com.zano.shareride.network.common.EnumStatus;

import org.joda.time.LocalDate;

/**
 * Created by Zano on 10/09/2017, 17:58.
 */

public class UserRequestListRequest extends BaseRequest {

    private String userId;
    private LocalDate date;
    private EnumStatus status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public EnumStatus getStatus() {
        return status;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UserRequestListRequest{" +
                "userId='" + userId + '\'' +
                ", date=" + date +
                ", status=" + status +
                '}';
    }
}
