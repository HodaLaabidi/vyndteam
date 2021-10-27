package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;

/**
 * Created by S4M37 on 21/07/2016.
 */
public class Hour implements Serializable {

    public String closeTime;
    public String openTime;

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getOpenTime() {
        return openTime.substring(0, openTime.length() - 3);
    }

    public String getCloseTime() {
        return closeTime.substring(0, closeTime.length() - 3);
    }

    @Override
    public String toString() {
        return "Hour{" +
                "closeTime='" + closeTime + '\'' +
                ", openTime='" + openTime + '\'' +
                '}';
    }
}
