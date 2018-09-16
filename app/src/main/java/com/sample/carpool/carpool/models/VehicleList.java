package com.sample.carpool.carpool.models;

/**
 * Created by akash on 08-03-2018.
 */

public class VehicleList {

    public String registration_no;

    public VehicleList() {
    }

    public VehicleList(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }
}
