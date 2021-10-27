package com.vyndsolutions.vyndteam.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.plumillonforge.android.chipview.Chip;

import java.io.Serializable;

/**
 * Created by Hoda on 15/03/2018.
 */

public class ValidInfoChip implements Chip, Serializable, Parcelable {

    public static final Creator<ValidInfoChip> CREATOR = new Creator<ValidInfoChip>() {
        @Override
        public ValidInfoChip createFromParcel(Parcel in) {
            return new ValidInfoChip(in);
        }

        @Override
        public ValidInfoChip[] newArray(int size) {
            return new ValidInfoChip[size];
        }
    };
    public int id;
    public String label;
    private int type;
    private boolean isSelected;

    public ValidInfoChip() {
    }

    public ValidInfoChip(String label) {

        this.label = label;
    }

    public ValidInfoChip(String label, int type, boolean isSelected) {
        this.label = label;
        this.type = type;
        this.isSelected = isSelected;
    }

    protected ValidInfoChip(Parcel in) {
        id = in.readInt();
        label = in.readString();
        if (type != 0)
            type = in.readInt();
        isSelected = in.readByte() != 0;
    }

    @Override
    public String getText() {
        return label;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(label);
        parcel.writeInt(type);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}
