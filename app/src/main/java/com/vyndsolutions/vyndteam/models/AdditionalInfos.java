package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hoda on 08/05/2018.
 */

public class AdditionalInfos implements Serializable {
    int id;

    boolean value;
    ValidInfo validInfo;

    public AdditionalInfos(int id, boolean value, ValidInfo validInfo) {
        this.id = id;
        this.value = value;
        this.validInfo = validInfo;
    }

    public AdditionalInfos(){
        this.id = 0 ;
        this.value = false ;
        this.validInfo  = new ValidInfo();
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


}
