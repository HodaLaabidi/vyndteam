package com.vyndsolutions.vyndteam.models.search;

import com.google.gson.annotations.SerializedName;
import com.vyndsolutions.vyndteam.models.SubCategory;

import java.io.Serializable;

public class AutoComplete implements Serializable {
    public boolean checked = false;
    int id;
    String libelle;
    String type;
    @SerializedName("icon")
    String icone;

    public AutoComplete(SubCategory subCategory) {
        this.id = (int) subCategory.getId();
        this.type = "2";
        this.libelle = subCategory.getName();

    }

    public AutoComplete() {

    }

    public AutoComplete(int id, String libelle, String type) {
        this.id = id;
        this.libelle = libelle;
        this.type = type;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
