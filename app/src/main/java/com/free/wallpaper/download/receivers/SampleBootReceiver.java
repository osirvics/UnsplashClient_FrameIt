package com.free.wallpaper.download.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.free.wallpaper.download.helpers.WallpaperScheduler;



public class SampleBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WallpaperScheduler alarm = new WallpaperScheduler();
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
           // SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            //boolean state =  preferences.getBoolean("daily_wallpaper", false);
            //if(state){
                //Toast.makeText(context,"Setting alarm", Toast.LENGTH_SHORT).show();
                alarm.setAlarm(context);
            //}
//            if(!state) {
//                Toast.makeText(context, "Not Setting alarm", Toast.LENGTH_SHORT).show();
//            }
        }
    }
}
