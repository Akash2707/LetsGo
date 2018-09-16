package com.sample.carpool.carpool.models;

/**
 * Created by akash on 10-04-2018.
 */

public class TokenDetails {

    public String user_id;
    public String token_Id;

    public TokenDetails() {
    }

    public TokenDetails(String user_id, String token_Id) {
        this.user_id = user_id;
        this.token_Id = token_Id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken_Id() {
        return token_Id;
    }

    public void setToken_Id(String token_Id) {
        this.token_Id = token_Id;
    }
}
