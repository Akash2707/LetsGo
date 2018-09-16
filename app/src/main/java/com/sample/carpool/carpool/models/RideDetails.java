package com.sample.carpool.carpool.models;

/**
 * Created by akash on 01-03-2018.
 */

public class RideDetails {

    public String ride_id;
    public String driver_id;
    public String vehicle_id;
    public String vehicle_owner_id;
    public String start_location;
    public String end_location;
    public String start_time;
    public String start_date;
    public String return_journey;
    public String return_time;
    public String ride_type;
    public String price_per_km;
    public String available_seats;
    public String seats_occupied;
    public String total_km;
    public String total_price;
    public String car_name;

    public RideDetails() {
    }

    public RideDetails(String ride_id, String driver_id, String vehicle_id, String vehicle_owner_id, String start_location, String end_location, String start_time, String start_date, String return_journey, String return_time, String ride_type, String price_per_km, String available_seats, String seats_occupied, String total_km, String total_price, String car_name) {
        this.ride_id = ride_id;
        this.driver_id = driver_id;
        this.vehicle_id = vehicle_id;
        this.vehicle_owner_id = vehicle_owner_id;
        this.start_location = start_location;
        this.end_location = end_location;
        this.start_time = start_time;
        this.start_date = start_date;
        this.return_journey = return_journey;
        this.return_time = return_time;
        this.ride_type = ride_type;
        this.price_per_km = price_per_km;
        this.available_seats = available_seats;
        this.seats_occupied = seats_occupied;
        this.total_km = total_km;
        this.total_price = total_price;
        this.car_name = car_name;
    }

    public String getRide_id() {
        return ride_id;
    }

    public void setRide_id(String ride_id) {
        this.ride_id = ride_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getVehicle_owner_id() {
        return vehicle_owner_id;
    }

    public void setVehicle_owner_id(String vehicle_owner_id) {
        this.vehicle_owner_id = vehicle_owner_id;
    }

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

    public String getReturn_journey() {
        return return_journey;
    }

    public void setReturn_journey(String return_journey) {
        this.return_journey = return_journey;
    }

    public String getReturn_time() {
        return return_time;
    }

    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }

    public String getRide_type() {
        return ride_type;
    }

    public void setRide_type(String ride_type) {
        this.ride_type = ride_type;
    }

    public String getPrice_per_km() {
        return price_per_km;
    }

    public void setPrice_per_km(String price_per_km) {
        this.price_per_km = price_per_km;
    }

    public String getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(String available_seats) {
        this.available_seats = available_seats;
    }

    public String getSeats_occupied() {
        return seats_occupied;
    }

    public void setSeats_occupied(String seats_occupied) {
        this.seats_occupied = seats_occupied;
    }

    public String getTotal_km() {
        return total_km;
    }

    public void setTotal_km(String total_km) {
        this.total_km = total_km;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }
}
