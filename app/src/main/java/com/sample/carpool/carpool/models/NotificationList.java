package com.sample.carpool.carpool.models;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationList {

    public String s_name;
    public String s_gender;
    public String s_phone;
    public String pass_number;
    public String sender_id;
    public String receiver_id;

    public NotificationList() {
    }

    public NotificationList(String s_name, String s_gender, String s_phone, String pass_number, String sender_id, String receiver_id) {
        this.s_name = s_name;
        this.s_gender = s_gender;
        this.s_phone = s_phone;
        this.pass_number = pass_number;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
    }


    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getS_gender() {
        return s_gender;
    }

    public void setS_gender(String s_gender) {
        this.s_gender = s_gender;
    }

    public String getS_phone() {
        return s_phone;
    }

    public void setS_phone(String s_phone) {
        this.s_phone = s_phone;
    }

    public String getPass_number() {
        return pass_number;
    }

    public void setPass_number(String pass_number) {
        this.pass_number = pass_number;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }
}
