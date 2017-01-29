package com.noddyandfriends.disastermanager.util;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.noddyandfriends.disastermanager.R;

public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = "Pushy";
        String notificationText = "Test notification";

        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        if (intent.getStringExtra("message") != null) {
            notificationText = intent.getStringExtra("message");
        }
    }

    private void NotifyRefugeMatched(Context context, String hostName, String address, long latitude, long longitude){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("You have been matched");
        builder.setContentText("You are being accomodated by " +hostName+" at "+ address);
        builder.setSmallIcon(R.drawable.home);

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
