package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;

/**
 * Created by Hoda on 02/05/2018.
 */

public class Day implements Serializable {
    String dayName;
    int number;

    public Day(String dayName, int number){
        this.dayName = dayName;
        this.number = number;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
