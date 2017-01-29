package com.noddyandfriends.disastermanager;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.noddyandfriends.disastermanager.util.Constants;

import java.net.URL;

import me.pushy.sdk.Pushy;

import static com.noddyandfriends.disastermanager.util.Constants.DEVICE_TOKEN;
import static com.noddyandfriends.disastermanager.util.Constants.USER_NAME;
import static com.noddyandfriends.disastermanager.util.Constants.USER_PREFS;

public class MainActivity extends AppCompatActivity {
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_main);
        Pushy.listen(this);
        // Check whether the user has granted us the READ/WRITE_EXTERNAL_STORAGE permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        boolean isFirstStart = true;
        SharedPreferences preferences = getSharedPreferences(USER_PREFS, 0);
        if(preferences.contains(DEVICE_TOKEN) && preferences.contains(USER_NAME)){
            isFirstStart = false;
        }
        //Else, show the distress window
        if(isFirstStart){
            onFirstStart();
        }else{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.activity_main, new ReportDisasterFragment());
            transaction.commit();
        }
    }

    protected void onFirstStart(){
        new RegisterForPushNotificationsAsync().execute();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_main, new EnterDetailsFragment());
        transaction.commit();
    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Exception> {
        private String deviceToken = null;
        protected Exception doInBackground(Void... params) {
            try {
                // Assign a unique token to this device
                SharedPreferences preferences = getSharedPreferences(USER_PREFS, 0);
                if(!Pushy.isRegistered(activity)){
                    deviceToken = Pushy.register(activity);
                }
                // Log it for debugging purposes
                Log.d("MyApp", "Pushy device token: " + deviceToken);

                // Send the token to your backend server via an HTTP GET request
                new URL("https://"+ Constants.HOST_URI+"/register/device?token=" + deviceToken).openConnection();
            }
            catch (Exception exc) {
                // Return exc to onPostExecute
                return exc;
            }
            // Success
            return null;
        }

        @Override
        protected void onPostExecute(Exception exc) {
            // Failed?
            if (exc != null) {
                // Show error as toast message
                Toast.makeText(getApplicationContext(), exc.toString(), Toast.LENGTH_LONG).show();
                return;
            }

            // Succeeded, do something to alert the user
            SharedPreferences preferences = getSharedPreferences(USER_PREFS, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(DEVICE_TOKEN, deviceToken);
            editor.apply();
        }
    }
}
