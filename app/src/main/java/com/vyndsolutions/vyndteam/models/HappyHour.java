package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Ismail on 19/10/2017.
 */

public class HappyHour implements Serializable {

    private Business business;
    private Hour[] hours;
    private int dayOfTheWeek;
    private String description;
    private boolean isAvailable;

    public String getStartingHour() {
        if (hours != null && hours.length != 0)
            return hours[0].getOpenTime();
        else return "";
    }
    public String getCloseHour() {
        if (hours != null && hours.length != 0)
            return hours[0].getCloseTime();
        else return "";
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
    public Hour[] getHourss() {
        return hours;
    }

    public void setHours(Hour[] hours) {
        this.hours = hours;
    }

    public String getHours2() {
        String hours = "";
        if (this.hours.length == 0 || this.hours[0].getOpenTime().equals(this.hours[0].getCloseTime())) {
            return "";
        }
        if (this.hours.length > 0) {
            hours += this.hours[0].getOpenTime() + " - " + this.hours[0].getCloseTime();
        }
        if (this.hours.length > 1) {
            hours += "\n" + this.hours[1].getOpenTime() + " - " + this.hours[1].getCloseTime();
        }

        return hours;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
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


    @Override
    public String toString() {
        return "HappyHour{" +
                "hours=" + Arrays.toString(hours) +
                ", dayOfTheWeek=" + dayOfTheWeek +
                ", description='" + description + '\'' +
                ", isAvailable=" + isAvailable +
                '}';
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

}
