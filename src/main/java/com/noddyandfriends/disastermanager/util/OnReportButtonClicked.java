package com.noddyandfriends.disastermanager.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.noddyandfriends.disastermanager.R;
import com.noddyandfriends.disastermanager.model.User;

/**
 * Created by anant on 28/1/17.
 */

public class OnReportButtonClicked implements View.OnClickListener{
    private static String LOG_TAG = OnReportButtonClicked.class.getName();
    private Context context;
    private LocationManager manager;
    private String networkProvider;
    private Location location;
    private String disasterType;
    private OnReportButtonClicked(){}
    public static OnReportButtonClicked listenerWithContext(Context context){
        OnReportButtonClicked instance = new OnReportButtonClicked();
        instance.context = context;
        instance.manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        instance.networkProvider = LocationManager.NETWORK_PROVIDER;
        return instance;
    }
    public OnReportButtonClicked andDisasterType(String str) {
        this.disasterType = str;
        return this;
    }
    @Override
    public void onClick(View view) {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_DENIED){
            location = manager.getLastKnownLocation(networkProvider);
        }else{
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission_group.LOCATION}, context.getResources().getInteger(R.integer.PERMISSION_ACCESS_LOCATION));
        }
        if(location != null){
            Uri uri = Constants.HOST_URI;
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    uri.toString(),
                    User.userToJson(User.userFromSharedPreferences(context), location.getLatitude(), location.getLongitude(), "foo"),
                    null,
                    null
            );
            AppController.getInstance().addToRequestQueue(request);
        }
        if(ContextCompat.checkSelfPermission(context, Manifest.permission_group.SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, 1324);
        }
        User self = User.userFromSharedPreferences(context);
        SmsManager manager = SmsManager.getDefault();
        String smsText = new StringBuilder()
                .append(self.getName())
                .append(" is caught in a ")
                .append(disasterType)
                .append(" at location: ")
                .append(location.getLatitude())
                .append(", ")
                .append(location.getLongitude())
                .toString();
        manager.sendTextMessage(self.getEmergencyTelephone(), null, smsText, null, null);
    }
}

