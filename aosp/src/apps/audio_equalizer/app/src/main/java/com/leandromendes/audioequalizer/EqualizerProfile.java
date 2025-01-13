package com.leandromendes.audioequalizer;

import android.os.Parcel;
import android.os.Parcelable;

public class EqualizerProfile implements Parcelable {

    private String name;
    private int bassEqValue;
    private int midEqValue;
    private int hiEqValue;
    private int balanceEqValue;
    private int masterVolValue;

    public EqualizerProfile() {
        this.name = "Default";
        this.bassEqValue = 0;
        this.midEqValue = 0;
        this.hiEqValue = 0;
        this.balanceEqValue = 5;
        this.masterVolValue = 6;
    }

    public EqualizerProfile(String name, int bassEqValue, int midEqValue, int hiEqValue,
                            int panEqValue, int maVolValue) {
        this.name = name;
        this.bassEqValue = bassEqValue;
        this.midEqValue = midEqValue;
        this.hiEqValue = hiEqValue;
        this.balanceEqValue = panEqValue;
        this.masterVolValue = maVolValue;
    }

    public int getBalanceEqValue() {
        return balanceEqValue;
    }

    public void setBalanceEqValue(int balanceEqValue) {
        this.balanceEqValue = balanceEqValue;
    }

    public int getMasterVolValue() {
        return masterVolValue;
    }

    public void setMasterVolValue(int masterVolValue) {
        this.masterVolValue = masterVolValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBassEqValue() {
        return bassEqValue;
    }

    public void setBassEqValue(int bassEqValue) {
        this.bassEqValue = bassEqValue;
    }

    public int getMidEqValue() {
        return midEqValue;
    }

    public void setMidEqValue(int midEqValue) {
        this.midEqValue = midEqValue;
    }

    public int getHiEqValue() {
        return hiEqValue;
    }

    public void setHiEqValue(int hiEqValue) {
        this.hiEqValue = hiEqValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.bassEqValue);
        dest.writeInt(this.midEqValue);
        dest.writeInt(this.hiEqValue);
        dest.writeInt(this.balanceEqValue);
        dest.writeInt(this.masterVolValue);
    }

    public static final Creator<EqualizerProfile> CREATOR = new Creator<EqualizerProfile>() {
        @Override
        public EqualizerProfile createFromParcel(Parcel source) {
            String name = source.readString();
            int bassEqValue = source.readInt();
            int midEqValue = source.readInt();
            int hiEqValue = source.readInt();
            int panEqValue = source.readInt();
            int maVolValue = source.readInt();
            return new EqualizerProfile(name,
                        bassEqValue,
                        midEqValue,
                        hiEqValue,
                        panEqValue,
                        maVolValue);
        }

        @Override
        public EqualizerProfile[] newArray(int size) {
            return new EqualizerProfile[size];
        }
    };
}
