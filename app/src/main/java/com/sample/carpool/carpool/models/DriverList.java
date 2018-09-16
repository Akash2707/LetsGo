package com.sample.carpool.carpool.models;

/**
 * Created by akash on 05-04-2018.
 */

public class DriverList {

    public String licence_no;

    public DriverList() {
    }

    public DriverList(String licence_no) {
        this.licence_no = licence_no;
    }

    public String getLicence_no() {
        return licence_no;
    }

    public void setLicence_no(String licence_no) {
        this.licence_no = licence_no;
    }
}

