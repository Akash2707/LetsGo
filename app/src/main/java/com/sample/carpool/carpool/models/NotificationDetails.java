package com.sample.carpool.carpool.models;

public class NotificationDetails {

    public String receiver_token_id;
    public String sender_token_id;
    public String receiver_id;
    public String sender_id;
    public String sender_name;
    public String sender_gender;
    public String sender_mobile;
    public String passenger_no;

    public NotificationDetails() {
    }

    public NotificationDetails(String receiver_token_id, String sender_token_id, String receiver_id, String sender_id, String sender_name, String sender_gender, String sender_mobile, String passenger_no) {
        this.receiver_token_id = receiver_token_id;
        this.sender_token_id = sender_token_id;
        this.receiver_id = receiver_id;
        this.sender_id = sender_id;
        this.sender_name = sender_name;
        this.sender_gender = sender_gender;
        this.sender_mobile = sender_mobile;
        this.passenger_no = passenger_no;
    }

    public String getReceiver_token_id() {
        return receiver_token_id;
    }

    public void setReceiver_token_id(String receiver_token_id) {
        this.receiver_token_id = receiver_token_id;
    }

    public String getSender_token_id() {
        return sender_token_id;
    }

    public void setSender_token_id(String sender_token_id) {
        this.sender_token_id = sender_token_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_gender() {
        return sender_gender;
    }

    public void setSender_gender(String sender_gender) {
        this.sender_gender = sender_gender;
    }

    public String getSender_mobile() {
        return sender_mobile;
    }

    public void setSender_mobile(String sender_mobile) {
        this.sender_mobile = sender_mobile;
    }

    public String getPassenger_no() {
        return passenger_no;
    }

    public void setPassenger_no(String passenger_no) {
        this.passenger_no = passenger_no;
    }
}
