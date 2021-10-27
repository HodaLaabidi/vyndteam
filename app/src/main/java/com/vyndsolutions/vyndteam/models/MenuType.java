package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hoda on 06/03/2018.
 */

public class MenuType implements Serializable{
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String label ;
    String description ;
    int id ;
    ArrayList<MenuProduct> products;

    public ArrayList<MenuProduct> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<MenuProduct> products) {
        this.products = products;
    }



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

    public int getBusinessId() {
        return id;
    }

    public void setBusinessId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MenuType{" +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", products=" + products +
                '}';
    }
}
