package com.sample.carpool.carpool.models;

/**
 * Created by akash on 25-02-2018.
 */

public class UserDetails {

    public String first_name;
    public String last_name;
    public String email_id;
    public String gender;
    public String mobile_number;
    public String image_url;

    public UserDetails() {
    }

    public UserDetails(String first_name, String last_name, String email_id, String gender, String mobile_number, String image_url) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_id = email_id;
        this.gender = gender;
        this.mobile_number = mobile_number;
        this.image_url = image_url;

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}