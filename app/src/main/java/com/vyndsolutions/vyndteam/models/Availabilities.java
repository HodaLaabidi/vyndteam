package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Hoda on 20/03/2018.
 */

public class Availabilities implements Serializable {

    public Hour[] hours;
    public int dayOfTheWeek;
    public String description;
    public String hourOpenDay;
    public String hourCloseDay;
    public String hourOpenNight;
    public String hourCloseNight;
    public boolean isAvailable;
    public Availabilities(int dayOfTheWeek, boolean isAvailable) {
        this.dayOfTheWeek = dayOfTheWeek;
        this.isAvailable = isAvailable;
    }
    public Availabilities(int dayOfTheWeek, String description , boolean isAvailable) {
        this.dayOfTheWeek = dayOfTheWeek;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    public Availabilities() {
    }

    public void setHours(Hour[] hours) {
        this.hours = hours;
    }

    public int getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getHours() {
        String hours = "";
        if (this.hours.length == 0) {
            return "Fermé toute la journée";
        }
        if (this.hours.length > 0) {
            hours += "de " + this.hours[0].getOpenTime() + " à " + this.hours[0].getCloseTime();
        }
        if (this.hours.length > 1) {
            hours += " - de " + this.hours[1].getOpenTime() + " à " + this.hours[1].getCloseTime();
        }

        return hours;
    }
    public String getHoursWorking() {
        String hours = "";
        if (this.hours.length == 0) {
            return "Fermé";
        }
        if (this.hours.length > 0) {
            hours +=this.hours[0].getOpenTime() + " à " + this.hours[0].getCloseTime();
        }
        if (this.hours.length > 1) {
            hours += " - " + this.hours[1].getOpenTime() + " à " + this.hours[1].getCloseTime();
        }

        return hours;
    }
    public String getOpenHourDay()
    {String hours="";
        if (this.hours.length > 0)
            hours =this.hours[0].getOpenTime();
        else hours="-:-";
        return hours;
    }
    public String getCloseHourDay()
    {String hours="";
        if (this.hours.length > 0)
            hours =this.hours[0].getCloseTime();
        else hours="-:-";
        return hours;
    }
    public String getOpenHourNight()
    {String hours="";
        if (this.hours.length > 1)
            hours = this.hours[1].getOpenTime();
        else hours="-:-";
        return hours;
    }
    public String getCloseHourNight()
    {String hours="";
        if (this.hours.length > 1)
            hours = this.hours[1].getCloseTime();
        else hours="-:-";
            return hours;
    }

    public boolean isClosed() {
        try {
            if (hours.length == 0) {
                return true;
            }
            return hours[0].openTime.equals(hours[0].closeTime);
        }catch (NullPointerException e)
        {
            return false;
        }


    }

    @Override
    public String toString() {
        return "Availabilities{" +
                "hours=" + Arrays.toString(hours) +
                ", dayOfTheWeek=" + dayOfTheWeek +
                ", isAvailable=" + isAvailable +
                '}';
    }
    private void prepareDataToSend()
    {

    }
}
