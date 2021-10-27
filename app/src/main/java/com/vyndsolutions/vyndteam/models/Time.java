package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;

/**
 * Created by Hoda on 06/05/2018.
 */

public class Time implements Serializable {

    private  String openTime;
    private  String closeTime;

    public Time(String openTime, String closeTime) {
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public Time(){
        this.openTime ="";
        this.openTime ="";
    }

    public String getOpenTime() {

        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
}
