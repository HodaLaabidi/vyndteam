package com.vyndsolutions.vyndteam.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Hoda on 14/03/2018.
 */

public class ValidInfo implements Serializable, Parcelable {
    public static final Creator<ValidInfo> CREATOR = new Creator<ValidInfo>() {
        @Override
        public ValidInfo createFromParcel(Parcel in) {
            return new ValidInfo(in);
        }

        @Override
        public ValidInfo[] newArray(int size) {
            return new ValidInfo[size];
        }
    };
    public int id;
    public String label;
    public String icon;
    public String iconDisabled;
    public boolean valid;
    private int type;
    private int validInfoType;
    private int iconResource;
    private boolean hasInfo;
    private boolean isSelected;

    protected ValidInfo(Parcel in) {
        id = in.readInt();
        label = in.readString();
        icon = in.readString();
        iconDisabled = in.readString();
        valid = in.readByte() != 0;
        type = in.readInt();
        validInfoType = in.readInt();
        iconResource = in.readInt();
        hasInfo = in.readByte() != 0;
        isSelected = in.readByte() != 0;
    }

    public ValidInfo() {
    }

    public ValidInfo(int id, String label, String icon, int type) {
        this.id = id;
        this.label = label;
        this.icon = icon;
        this.validInfoType = type;
    }

    public ValidInfo(int id, String label, int iconResource) {
        this.id = id;
        this.label = label;
       this.iconResource = iconResource;
    }

    public int getValidInfoType() {
        return validInfoType;
    }

   public int getIconResource() {
        return iconResource;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean hasInfo() {
        return hasInfo;
    }

    public void setHasInfo(boolean hasInfo) {
        this.hasInfo = hasInfo;
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

    public String getIconDisabled() {
        return iconDisabled;
    }

    public void setIconDisabled(String iconDisabled) {
        this.iconDisabled = iconDisabled;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ValidInfo{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", icon='" + icon + '\'' +
                ", iconDisabled='" + iconDisabled + '\'' +
                ", valid=" + valid +
                ", type=" + type +
                ", validInfoType=" + validInfoType +
                ", iconResource=" + iconResource +
                ", hasInfo=" + hasInfo +
                ", isSelected=" + isSelected +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(label);
        parcel.writeString(icon);
        parcel.writeString(iconDisabled);
        parcel.writeByte((byte) (valid ? 1 : 0));
        parcel.writeInt(type);
        parcel.writeInt(validInfoType);
        parcel.writeInt(iconResource);
        parcel.writeByte((byte) (hasInfo ? 1 : 0));
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}

