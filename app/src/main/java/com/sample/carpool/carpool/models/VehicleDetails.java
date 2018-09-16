package com.sample.carpool.carpool.models;

/**
 * Created by akash on 28-02-2018.
 */

public class VehicleDetails {

    public String vehicle_id;
    public String vehicle_owner_id;
    public String registration_no;
    public String seats_no;
    public String car_type;
    public String car_name;
    public String car_brand;
    public String fuel_type;


    public VehicleDetails() {
    }

    public VehicleDetails(String vehicle_id, String vehicle_owner_id, String registration_no, String seats_no, String car_type, String car_name, String car_brand, String fuel_type) {
        this.vehicle_id = vehicle_id;
        this.vehicle_owner_id = vehicle_owner_id;
        this.registration_no = registration_no;
        this.seats_no = seats_no;
        this.car_type = car_type;
        this.car_name = car_name;
        this.car_brand = car_brand;
        this.fuel_type = fuel_type;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getRegistration_no() {
        return registration_no;
    }

    public void setRegistration_no(String registration_no) {
        this.registration_no = registration_no;
    }

    public String getSeats_no() {
        return seats_no;
    }

    public void setSeats_no(String seats_no) {
        this.seats_no = seats_no;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getCar_brand() {
        return car_brand;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getVehicle_owner_id() {
        return vehicle_owner_id;
    }

    public void setVehicle_owner_id(String vehicle_owner_id) {
        this.vehicle_owner_id = vehicle_owner_id;
    }
}
