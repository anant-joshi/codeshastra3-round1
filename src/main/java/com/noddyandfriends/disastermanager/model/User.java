package com.noddyandfriends.disastermanager.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.noddyandfriends.disastermanager.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by anant on 28/1/17.
 */

public class User {
    private static String LOG_TAG = User.class.getName();
    private String name;
    private String address;
    private String emergencyTelephone;
    private String emergencyContactName;
    private boolean isFemale;
    private Date dateOfBirth;


    public User(
            String name,
            String address,
            String emergencyContactName,
            String emergencyTelephone,
            boolean isFemale,
            Date dateOfBirth
    ) {
        this.name = name;
        this.address = address;
        this.emergencyContactName = emergencyContactName;
        this.emergencyTelephone = emergencyTelephone;
        this.isFemale = isFemale;
        this.dateOfBirth = dateOfBirth;
    }

    public static JSONObject userToJson(User user, double lat, double lon, String disasterType) {
        JSONObject userObject = new JSONObject();
        try {
            userObject.put(Constants.USER_NAME, user.name);
            userObject.put(Constants.USER_ADDRESS, user.address);
            userObject.put(Constants.USER_EMERGENCY_NAME, user.emergencyContactName);
            userObject.put(Constants.USER_EMERGENCY_TEL, user.emergencyTelephone);
            userObject.put(Constants.USER_SEX, user.isFemale);
            userObject.put(Constants.USER_DATE_OF_BIRTH, user.dateOfBirth.getTime());
            userObject.put(Constants.LATITUDE, lat);
            userObject.put(Constants.LONGITUDE, lon);
            userObject.put(Constants.DISASTER_TYPE, disasterType);
        } catch (JSONException je) {
            Log.e(LOG_TAG, je.getMessage());
        }
        return userObject;
    }

    public static User jsonToUser(JSONObject object) {
        User user = null;
        try {
            user = new User(
                    object.getString(Constants.USER_NAME),
                    object.getString(Constants.USER_ADDRESS),
                    object.getString(Constants.USER_EMERGENCY_NAME),
                    object.getString(Constants.USER_EMERGENCY_TEL),
                    object.getBoolean(Constants.USER_SEX),
                    new Date(object.getLong(Constants.USER_DATE_OF_BIRTH))
                    );
        } catch (JSONException je) {
            Log.e(LOG_TAG, je.getMessage());
        }
        return user;
    }

    public static User userFromSharedPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Constants.USER_PREFS, 0);
        String name = preferences.getString(Constants.USER_NAME, null);
        String address = preferences.getString(Constants.USER_ADDRESS, null);
        String emergencyName = preferences.getString(Constants.USER_EMERGENCY_NAME, null);
        String emergencyTelephone = preferences.getString(Constants.USER_EMERGENCY_TEL, null);
        boolean isFemale = preferences.getBoolean(Constants.USER_SEX, true);
        Date dateOfBirth = new Date(preferences.getLong(Constants.USER_DATE_OF_BIRTH, 0));

        return new User(
                name,
                address,
                emergencyName,
                emergencyTelephone,
                isFemale,
                dateOfBirth
        );
    }

    public String getAddress() {
        return address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public String getEmergencyTelephone() {
        return emergencyTelephone;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public static String getLogTag() {
        return LOG_TAG;
    }

    public String getName() {
        return name;
    }
}
