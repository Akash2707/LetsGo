package com.sample.carpool.carpool.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by akash on 03-03-2018.
 */

public class SelectRide implements Parcelable {

    public String start_location;
    public String end_location;
    public String start_time;
    public String start_date;
    public String available_seats;
    public String car_name;
    public String total_price;
    public String vehicle_owner_id;

    public SelectRide() {
    }

    public SelectRide(String start_location, String end_location, String start_time, String start_date, String available_seats, String car_name, String total_price, String vehicle_owner_id) {
        this.start_location = start_location;
        this.end_location = end_location;
        this.start_time = start_time;
        this.start_date = start_date;
        this.available_seats = available_seats;
        this.car_name = car_name;
        this.total_price = total_price;
        this.vehicle_owner_id = vehicle_owner_id;
    }

    protected SelectRide(Parcel in) {
        start_location = in.readString();
        end_location = in.readString();
        start_time = in.readString();
        start_date = in.readString();
        available_seats = in.readString();
        car_name = in.readString();
        total_price = in.readString();
        vehicle_owner_id=in.readString();
    }

    public static final Creator<SelectRide> CREATOR = new Creator<SelectRide>() {
        @Override
        public SelectRide createFromParcel(Parcel in) {
            return new SelectRide(in);
        }

        @Override
        public SelectRide[] newArray(int size) {
            return new SelectRide[size];
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

    public String getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(String available_seats) {
        this.available_seats = available_seats;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getVehicle_owner_id() {
        return vehicle_owner_id;
    }

    public void setVehicle_owner_id(String vehicle_owner_id) {
        this.vehicle_owner_id = vehicle_owner_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(start_location);
        parcel.writeString(end_location);
        parcel.writeString(start_time);
        parcel.writeString(start_date);
        parcel.writeString(available_seats);
        parcel.writeString(car_name);
        parcel.writeString(total_price);
        parcel.writeString(vehicle_owner_id);
    }
}
