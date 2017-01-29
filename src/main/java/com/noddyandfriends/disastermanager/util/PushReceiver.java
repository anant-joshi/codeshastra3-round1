package com.noddyandfriends.disastermanager.util;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.noddyandfriends.disastermanager.MainActivity;
import com.noddyandfriends.disastermanager.R;

import static com.noddyandfriends.disastermanager.util.Constants.REFUGE_ADDRESS;
import static com.noddyandfriends.disastermanager.util.Constants.REFUGE_LATITUDE;
import static com.noddyandfriends.disastermanager.util.Constants.REFUGE_LONGITUDE;
import static com.noddyandfriends.disastermanager.util.Constants.REFUGE_NAME;
import static com.noddyandfriends.disastermanager.util.Constants.USER_PREFS;

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = "Pushy";
        String notificationText = "Test notification";
        //data = {'type':'Shelter','name': fly.name,'address':fly.address,'lat':fly.lat,'lon':fly.lon}
        //data={'disaster':person.disaster,'lat':person.lat,'lon':person.lon}
        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        String type = intent.getStringExtra("type");
        if (type.equalsIgnoreCase("shelter")) {
            NotifyRefugeMatched(context, intent.getStringExtra("name"), intent.getStringExtra("address"), intent.getLongExtra("lat", 0), intent.getLongExtra("lon", 0));
        }else if (type.equalsIgnoreCase("disaster")){
            NotifyDisaster(intent.getStringExtra("disaster"), context);
        }
    }

    private void NotifyRefugeMatched(Context context, String hostName, String address, long latitude, long longitude){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("You have been matched");
        builder.setContentText("You are being accomodated by " +hostName+" at "+ address);
        builder.setSmallIcon(R.drawable.angel);
        MainActivity activity = (MainActivity) context;
        NavigationView menu = (NavigationView) activity.findViewById(R.id.nav_view);
        NavigationMenuItemView view = (NavigationMenuItemView) activity.findViewById(R.id.show_refuge);
        SharedPreferences.Editor editor = context.getSharedPreferences(USER_PREFS, 0).edit();
        editor.putString(REFUGE_NAME, hostName);
        editor.putString(REFUGE_ADDRESS, address);
        editor.putLong(REFUGE_LATITUDE, latitude);
        editor.putLong(REFUGE_LONGITUDE, longitude);
        editor.apply();
        view.setVisibility(View.VISIBLE);
    }

    private void NotifyDisaster(String disasterType, Context context){
        int icon;
        String title = "Disaster in your area";
        String subtitle;
        disasterType = disasterType.toLowerCase();
        switch(disasterType){
            case "earthquake":{
                icon = R.drawable.earthquake;
                subtitle = "Earthquake";
            }break;
            case "flood":{
                icon = R.drawable.flood;
                subtitle = "Flood";
            }break;
            case "cyclone":{
                icon = R.drawable.hurricane;
                subtitle = "Cyclone";
            }break;
            case "volcano":{
                icon = R.drawable.volcano;
                subtitle = "Volcano";
            }break;
            default:{
                icon = R.mipmap.ic_launcher;
                subtitle = "";
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setSmallIcon(icon);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(142, builder.build());
    }
}
