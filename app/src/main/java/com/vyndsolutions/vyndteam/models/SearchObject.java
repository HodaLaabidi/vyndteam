package com.vyndsolutions.vyndteam.models;


import java.io.Serializable;

/**
 * Created by Hoda on 11/04/2018.
 */

public class SearchObject implements Serializable {

    String keywordBusinessName;
    Region region;

    public SearchObject(String keywordBusinessName){
        this.keywordBusinessName = keywordBusinessName;
        this.region = new Region();
    }

    public SearchObject(String keywordBusinessName , Region region){
        this.keywordBusinessName = keywordBusinessName;
        this.region = region;
    }

    public String getKeywordBusinessName() {
        return keywordBusinessName;
    }

    public void setKeywordBusinessName(String keywordBusinessName) {
        this.keywordBusinessName = keywordBusinessName;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(int idRegion) {
        this.region = region;
    }


}
