package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;

/**
 * Created by Hoda on 21/03/2018.
 */

public class HoursHoraires implements Serializable {

    private String openTime = "";

    private int dayOfTheWeek = 0;

    private String closeTime = "";

    public int getDayOfTheWeek() {
       return  this.dayOfTheWeek;
    }

    public void setDayOfTheWeek(int dayOfTheWeek) {
         this.dayOfTheWeek = dayOfTheWeek;
    }



    public HoursHoraires() {

        this.openTime = "";
        this.closeTime = "";
        this.dayOfTheWeek = 0;
    }

    public HoursHoraires(String openTime, String closeTime , int dayOfTheWeek) {

        this.openTime = openTime;
        this.closeTime = closeTime;
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public HoursHoraires(String openTime, String closeTime ) {

        this.openTime = openTime;
        this.closeTime = closeTime;
        this.dayOfTheWeek = 6;
    }

    public String getOpenTime(){
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
