package com.noddyandfriends.disastermanager.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anant on 29/1/17.
 */

public class RequestSeeker {
    public static final String SEEKER_NAME = "seeker_name";
    public static final String SEEKER_AGE = "seeker_age";
    public static final String SEEKER_IS_FEMALE = "seeker_sex";
    private static final String LOG_TAG = RequestSeeker.class.getName();
    String name;
    int age;
    boolean isFemale;

    public RequestSeeker(int age, boolean isFemale, String name) {
        this.age = age;
        this.isFemale = isFemale;
        this.name = name;
    }

    public static JSONObject jsonFromSeeker(RequestSeeker seeker) {
        JSONObject object = new JSONObject();

        try {
            object.put(SEEKER_NAME, seeker.name);
            object.put(SEEKER_AGE, seeker.age);
            object.put(SEEKER_IS_FEMALE, seeker.isFemale);
        } catch (JSONException je) {
            Log.e(LOG_TAG, je.getMessage());
        }
        return object;
    }

    public static JSONArray jsonArrayFromSeekers(RequestSeeker[] seekers) {
        JSONArray array = new JSONArray();

        for (RequestSeeker seeker : seekers) {
            JSONObject object = jsonFromSeeker(seeker);
            array.put(object);
        }
        return array;
    }
}
