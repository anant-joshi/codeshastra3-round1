package com.noddyandfriends.disastermanager.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anant on 29/1/17.
 */

public class RequestSeekerGroup {
    private static final String LOG_TAG = RequestSeekerGroup.class.getName();
    private String deviceId;
    private int numberOfSeekers;
    private RequestSeeker[] seekers;

    public static final String SEEKER_GROUP_DEVICE_ID = "device_id";
    public static final String SEEKER_GROUP_NUMBER_OF = "num";
    public static final String SEEKER_GROUP_SEEKERS = "seekers";


    public RequestSeekerGroup(String dId, RequestSeeker[] seekers){
        this.seekers = seekers;
        this.deviceId = dId;
        this.numberOfSeekers = seekers.length;
    }

    public static JSONObject getJsonFromSeekerGroup(RequestSeekerGroup group){
        JSONObject object = new JSONObject();
        try{
            object.put(SEEKER_GROUP_DEVICE_ID, group.deviceId);
            object.put(SEEKER_GROUP_NUMBER_OF, group.numberOfSeekers);
            object.put(SEEKER_GROUP_SEEKERS, RequestSeeker.jsonArrayFromSeekers(group.seekers));
        }catch (JSONException je){
            Log.e(LOG_TAG, je.getMessage());
        }
        return object;
    }
}
