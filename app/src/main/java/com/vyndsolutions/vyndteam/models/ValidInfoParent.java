package com.vyndsolutions.vyndteam.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hoda on 16/05/2018.
 */


public class ValidInfoParent implements Serializable {
    private int id;
    private boolean value;
    private ValidInfo validInfo;

    public ValidInfoParent() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public ValidInfo getValidInfo() {
        return validInfo;
    }

    public void setValidInfo(ValidInfo validInfo) {
        this.validInfo = validInfo;
    }

    @Override
    public String toString() {
        return "ValidInfoParent{" +
                "id=" + id +
                ", value=" + value +
                ", validInfo=" + validInfo +
                '}';
    }
}
