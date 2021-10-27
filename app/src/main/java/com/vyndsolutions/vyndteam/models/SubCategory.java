package com.vyndsolutions.vyndteam.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Abbes on 29/02/2016.
 */
public class SubCategory implements Serializable {

    @SerializedName(value = "Id")
    int id;
    @SerializedName(value = "CategoryId")
    int categoryId;
    String name;
    @SerializedName(value = "CategoryName")
    String categoryName;
    String icon;

    public SubCategory(int id, String libelle) {
        this.categoryId = id;
        this.categoryName = libelle;
    }

    public SubCategory() {

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
