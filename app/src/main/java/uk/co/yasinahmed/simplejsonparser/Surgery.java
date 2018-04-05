package uk.co.yasinahmed.simplejsonparser;

// Created by yasinahmed on 22/03/2018.

import android.os.Parcel;
import android.os.Parcelable;

// To pass custom objects between activities you must implement Parcelable

class Surgery implements Parcelable {

    private Surgery(Parcel in) {
        surgeryID = in.readString();
        surgeryName = in.readString();
        surgeryImageURL = in.readString();
    }

    public static final Creator<Surgery> CREATOR = new Creator<Surgery>() {
        @Override
        public Surgery createFromParcel(Parcel in) {
            return new Surgery(in);
        }

        @Override
        public Surgery[] newArray(int size) {
            return new Surgery[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(surgeryID);
        parcel.writeString(surgeryName);
        parcel.writeString(surgeryImageURL);
    }

    private  String surgeryID;
    private String surgeryName;
    private String surgeryImageURL;

    Surgery(String surgeryID, String surgeryName, String surgeryImageURL) {
        this.surgeryID = surgeryID;
        this.surgeryName = surgeryName;
        this.surgeryImageURL = surgeryImageURL;
    }

    String getSurgeryID() {
        return surgeryID;
    }

    String getSurgeryName() {
        return surgeryName;
    }

    String getSurgeryImageURL() {
        return surgeryImageURL;
    }
}
