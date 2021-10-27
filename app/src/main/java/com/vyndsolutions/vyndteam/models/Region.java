package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;

/**
 * Created by Hoda on 07/03/2018.
 */

public class Region implements Serializable {

    int id ;
    String label ;
    int parent;
    int type;

    public Region(){
        this.id = 0;
        this.label = "";

    }

    public Region(int id, String label, int parent, int type) {
        this.id = id;
        this.label = label;
        this.parent = parent;
        this.type = type;
    }

    public Region(int id, String label, int type) {
        this.id = id;
        this.label = label;
        this.type = type;
    }

    public Region(int id, String label) {
        this.id=id;
        this.label=label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
