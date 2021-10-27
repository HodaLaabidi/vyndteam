package com.vyndsolutions.vyndteam.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hoda on 21/03/2018.
 */

public class AvailableTime implements Serializable , Parcelable{

    private int dayOfTheWeek ;

    private boolean isAvailable ;

    private String description ;
    private String openTime = "";

    public AvailableTime(String openTime, String closeTime,int dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public AvailableTime(String openTime, String closeTime) {
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

    private String closeTime = "";

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

    public boolean getAvailable(){ return isAvailable ;}
    public List<HoursHoraires> getHours() {
        return hours;
    }

    public void setHours(List<HoursHoraires> hours) {
        this.hours = hours;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AvailableTime() {
        dayOfTheWeek = 0;
        isAvailable = false ;
        this.description = "";

        hours = new ArrayList<HoursHoraires>();

    }
    // private String description ;

    private List<HoursHoraires> hours ;


    public AvailableTime(Parcel parcel) {

        this.dayOfTheWeek = parcel.readInt();
        this.isAvailable = parcel.readByte() != 0;
       // this.description = parcel.readString();
        parcel.readList(this.hours, HoursHoraires.class.getClassLoader());
    }

    public AvailableTime(int dayOfTheWeek , Boolean isAvailable , List<HoursHoraires> hours , String description){
        this.dayOfTheWeek = dayOfTheWeek ;
        this.isAvailable = isAvailable ;
        this.hours =hours;
        this.description = description;
    }
    public AvailableTime(int dayOfTheWeek , Boolean isAvailable , List<HoursHoraires> hours ){
        this.dayOfTheWeek = dayOfTheWeek ;
        this.isAvailable = isAvailable ;
        this.hours =hours;
        this.description = "";
    }


    public static final Creator<AvailableTime> CREATOR = new Creator<AvailableTime>() {
        @Override
        public AvailableTime createFromParcel(Parcel in) {
            return new AvailableTime(in);
        }

        @Override
        public AvailableTime[] newArray(int size) {
            return new AvailableTime[size];
        }
    };

    @Override
    public String toString() {
        return "AvailableTime{" +
                "dayOfTheWeek=" + dayOfTheWeek +
                ", isAvailable=" + isAvailable +
                ", description='" + description + '\'' +
                ", openTime='" + openTime + '\'' +
                ", closeTime='" + closeTime + '\'' +
                ", hours=" + hours +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(dayOfTheWeek);
        parcel.writeByte((byte) (isAvailable? 1:0));
       // parcel.writeString(description);
        parcel.writeList(hours);

    }
}
