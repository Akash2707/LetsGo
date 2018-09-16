package com.sample.carpool.carpool.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by akash on 07-03-2018.
 */

public class OfferList implements Parcelable {

    public String start_location;
    public String end_location;
    public String start_time;
    public String start_date;

    public OfferList() {
    }

    public OfferList(String start_location, String end_location, String start_time, String start_date) {
        this.start_location = start_location;
        this.end_location = end_location;
        this.start_time = start_time;
        this.start_date = start_date;
    }

    protected OfferList(Parcel in) {
        start_location = in.readString();
        end_location = in.readString();
        start_time = in.readString();
        start_date = in.readString();
    }

    public static final Creator<OfferList> CREATOR = new Creator<OfferList>() {
        @Override
        public OfferList createFromParcel(Parcel in) {
            return new OfferList(in);
        }

        @Override
        public OfferList[] newArray(int size) {
            return new OfferList[size];
        }
    };

    public String getStart_location() {
        return start_location;
    }

    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }

    public String getEnd_location() {
        return end_location;
    }

    public void setEnd_location(String end_location) {
        this.end_location = end_location;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(start_location);
        parcel.writeString(end_location);
        parcel.writeString(start_date);
        parcel.writeString(start_time);
    }
}
