package com.vyndsolutions.vyndteam.models;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by Hoda on 06/03/2018.
 */

public class ImageGalerie implements Serializable {

    int id ;
    String url;
    String urlxS;
    String urlxM ;
    String urlxL ;
    String thumbnailPath;
    private boolean isFromStorage;

    public ImageGalerie() {
    }

    public ImageGalerie(int size , Uri imageCroppedUri , Context context){

        Cursor cursor = null;
        this.url = imageCroppedUri.toString() ;
        this.urlxS = url;
        id = size;
        urlxL = "";
        urlxM = "";

    }
    public ImageGalerie(int id, String url, String thumbnailUrl, boolean isFromStorage) {
        this.id = id;
        this.url = url;
        this.thumbnailPath = thumbnailUrl;
        this.isFromStorage = isFromStorage;

    }

    public ImageGalerie(String url) {
        this.url = url;
    }

    public boolean isFromStorage() {
        return isFromStorage;
    }

    public void setFromStorage(boolean fromStorage) {
        isFromStorage = fromStorage;
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

    public String getImage() {
        return url;
    }

    public String getThumbnailPath() {
        return this.thumbnailPath;
    }
}
