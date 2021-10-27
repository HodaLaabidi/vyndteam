package com.vyndsolutions.vyndteam.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Vyndee on 23/05/2017.
 */

public class Classification implements Serializable, Comparable<Classification>, Parcelable {


    public static final Creator<Classification> CREATOR = new Creator<Classification>() {
        @Override
        public Classification createFromParcel(Parcel in) {
            return new Classification(in);
        }

        @Override
        public Classification[] newArray(int size) {
            return new Classification[size];
        }
    };
    private static String keyword = "";
    public int id;
    public String label;
    public int parentId;
    public String parentLabel;
    public int type;
    private int profileType;
    private String icon;


    private String iconDisabled;

    private String color = "#40aa8f";

    public Classification(int id, String label, int parentId, String parentLabel, int type) {
        this.id = id;
        this.label = label;
        this.parentId = parentId;
        this.parentLabel = parentLabel;
        this.type = type;
        color = "#40aa8f";
    }

    public Classification(int id, String label, int type) {
        this.id = id;
        this.label = label;

        this.type = type;
        color = "#40aa8f";
    }


    public Classification() {
    }

    protected Classification(Parcel in) {
        id = in.readInt();
        label = in.readString();
        parentId = in.readInt();
        parentLabel = in.readString();
        type = in.readInt();
        profileType = in.readInt();
        color = in.readString();
    }

    public static void setKeyword(String value) {
        keyword = value;
    }

    public String getColor() {
        if (type == 1)
            return color;
        else return "#87c4b2";
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getProfileType() {
        return profileType;
    }

    public void setProfileType(int profileType) {
        this.profileType = profileType;
    }

    public String getColorByType() {
        switch (profileType) {

            case 1:
                return "#348cce";

            case 2:
                return "#92cc71";

            case 3:
                return "#b557dd";

            default:
                return "#f05a75";


        }
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentLabel() {
        return parentLabel;
    }

    public void setParentLabel(String parentLabel) {
        this.parentLabel = parentLabel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return this.label;
    }

    public String getIconDisabled() {
        return iconDisabled;
    }

    public void setIconDisabled(String iconDisabled) {
        this.iconDisabled = iconDisabled;
    }

    @Override
    public int compareTo(Classification classification) {

        if (keyword != "") {
            if (classification.getLabel().toLowerCase().contains(keyword.toLowerCase())) {
                return 1;
            } else return -1;
        } else return 0;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        if (label == null)
            label = "";

        dest.writeString(label);
        dest.writeInt(parentId);

        if (parentLabel == null)
            parentLabel = "";

        dest.writeString(parentLabel);
        dest.writeInt(type);
        dest.writeInt(profileType);
        dest.writeString(color);

    }
}
