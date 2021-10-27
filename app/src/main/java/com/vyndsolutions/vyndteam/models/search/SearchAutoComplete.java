package com.vyndsolutions.vyndteam.models.search;

import java.io.Serializable;

/**
 * Created by Ismail on 16/11/2017.
 */

public class SearchAutoComplete implements Serializable {
    private int id;
    private String label;
    private String regionLabel;
    private String description;
    private String icon;
    private int type;

    public SearchAutoComplete(int id, String label, String regionLabel, String description, String icon, int type) {
        this.id = id;
        this.label = label;
        this.regionLabel = regionLabel;
        this.description = description;
        this.icon = icon;
        this.type = type;
    }

    public SearchAutoComplete(int id, String label, int type) {
        this.id = id;
        this.label = label;
        this.regionLabel = regionLabel;
        this.description = description;
        this.icon = icon;
        this.type = type;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRegionLabel() {
        return regionLabel;
    }

    public void setRegionLabel(String regionLabel) {
        this.regionLabel = regionLabel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
