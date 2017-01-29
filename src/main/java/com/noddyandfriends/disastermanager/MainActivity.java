package com.noddyandfriends.disastermanager;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.noddyandfriends.disastermanager.util.Constants;

import java.net.URL;

import me.pushy.sdk.Pushy;

import static com.noddyandfriends.disastermanager.util.Constants.USER_NAME;
import static com.noddyandfriends.disastermanager.util.Constants.USER_PREFS;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Activity activity;
    public boolean recievedSupport = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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
        if(preferences.contains(Constants.DEVICE_TOKEN) && preferences.contains(USER_NAME)){
            isFirstStart = false;
        }
        //Else, show the distress window
        if(isFirstStart){
            onFirstStart();
        }else{
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (id){
            case R.id.seek_refuge:{
                transaction.replace(R.id.activity_main, new ReportDisasterFragment());
            }break;
            default:{}
            case R.id.report_disaster:{
                transaction.replace(R.id.activity_main, new AskForShelterFragment());
            }break;

        }

        transaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            editor.putString(Constants.DEVICE_TOKEN, deviceToken);
            editor.apply();
        }
    }
}
