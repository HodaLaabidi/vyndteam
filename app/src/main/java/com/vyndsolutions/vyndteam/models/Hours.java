package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;

/**
 * Created by Hoda on 06/05/2018.
 */

public class Hours implements Serializable {

    private Time time;
    private int dayOfTheWeek;
    private String description;
    private boolean isAvailable;

    public Hours(){
        this.time = new Time();
        this.dayOfTheWeek = 0;
        this.description ="";
        this.isAvailable = false;
    }

    public Hours(Time time, int dayOfTheWeek, String description, boolean isAvailable) {
        this.time = time;
        this.dayOfTheWeek = dayOfTheWeek;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public int getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
