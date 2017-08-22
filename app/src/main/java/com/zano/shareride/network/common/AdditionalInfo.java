package com.zano.shareride.network.common;

/**
 * Created by Zano on 16/07/2017, 12:00.
 */

public class AdditionalInfo {

    private Integer numberOfSeats;
    private Boolean needAssistance;

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public Boolean getNeedAssistance() {
        return needAssistance;
    }

    public void setNeedAssistance(Boolean needAssistance) {
        this.needAssistance = needAssistance;
    }

    @Override
    public String toString() {
        return "AdditionalInfo{" +
                "numberOfSeats=" + numberOfSeats +
                ", needAssistance=" + needAssistance +
                '}';
    }
}
