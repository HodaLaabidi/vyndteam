package com.vyndsolutions.vyndteam.models;

import org.xmlpull.v1.XmlSerializer;

import java.io.Serializable;

/**
 * Created by Hoda on 05/04/2018.
 */

public class Img implements Serializable {

    private int id ;
    private String url ;
    private String urlxS;
    private String urlxM;
    private String urlxL ;
    public Img(){
        id = 0 ;
        url="";
        urlxS ="";
        urlxM ="";
        urlxL = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlxS() {
        return urlxS;
    }

    public void setUrlxS(String urlxS) {
        this.urlxS = urlxS;
    }

    public String getUrlxM() {
        return urlxM;
    }

    public void setUrlxM(String urlxM) {
        this.urlxM = urlxM;
    }

    public String getUrlxL() {
        return urlxL;
    }

    public void setUrlxL(String urlxL) {
        this.urlxL = urlxL;
    }
}
