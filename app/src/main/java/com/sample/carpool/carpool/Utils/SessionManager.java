package com.sample.carpool.carpool.Utils;

/**
 * Created by akash on 06-03-2018.
 */

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Pref";

    // User name (make variable public to access from outside)
    public static final String KEY_FNAME = "firstName";
    public static final String KEY_LNAME = "lastName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_URI = "imageURI";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void savedata(String fname,String lname , String email){
        // Storing name in pref
        editor.putString(KEY_FNAME, fname);
        editor.putString(KEY_LNAME,lname);
        editor.putString(KEY_EMAIL, email);


        // commit changes
        editor.commit();
    }

    public void saveImage(String imageUri){
        editor.putString(KEY_URI,imageUri);
        editor.commit();
    }

    public String fname(){
        return pref.getString(KEY_FNAME,"");
    }
    public String lname(){
        return pref.getString(KEY_LNAME,"");
    }
    public String mail(){
        return pref.getString(KEY_EMAIL,"");
    }
    public String image(){
        return pref.getString(KEY_URI,"");
    }
}