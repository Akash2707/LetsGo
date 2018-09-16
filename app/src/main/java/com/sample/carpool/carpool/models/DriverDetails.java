package com.sample.carpool.carpool.models;

/**
 * Created by akash on 28-02-2018.
 */

public class DriverDetails {

    public String driver_id;
    public String vehicle_owner_id;
    public String driver_name;
    public String licence_no;
    public String age;
    public String driver_gender;
    public String driver_mobile;
    public String driving_experince;

    public DriverDetails() {
    }

    public DriverDetails(String driver_id, String vehicle_owner_id, String driver_name, String licence_no, String age, String driver_gender, String driver_mobile, String driving_experince) {
        this.driver_id = driver_id;
        this.vehicle_owner_id = vehicle_owner_id;
        this.driver_name = driver_name;
        this.licence_no = licence_no;
        this.age = age;
        this.driver_gender = driver_gender;
        this.driver_mobile = driver_mobile;
        this.driving_experince = driving_experince;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getVehicle_owner_id() {
        return vehicle_owner_id;
    }

    public void setVehicle_owner_id(String vehicle_owner_id) {
        this.vehicle_owner_id = vehicle_owner_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getLicence_no() {
        return licence_no;
    }

    public void setLicence_no(String licence_no) {
        this.licence_no = licence_no;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDriver_gender() {
        return driver_gender;
    }

    public void setDriver_gender(String driver_gender) {
        this.driver_gender = driver_gender;
    }

    public String getDriver_mobile() {
        return driver_mobile;
    }

    public void setDriver_mobile(String driver_mobile) {
        this.driver_mobile = driver_mobile;
    }

    public String getDriving_experince() {
        return driving_experince;
    }

    public void setDriving_experince(String driving_experince) {
        this.driving_experince = driving_experince;
    }
}
