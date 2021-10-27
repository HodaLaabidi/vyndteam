package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hoda on 27/04/2018.
 */

public class MenuProduct implements Serializable{
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCategoryId() {
        return id;
    }

    public void setCategoryId(int id) {
        this.id = id;
    }

    String label;
    String description;
    float price;
    int id;
    ArrayList<ImageGalerie> images;

    public ArrayList<ImageGalerie> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageGalerie> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", id=" + id +
                '}';
    }
}
