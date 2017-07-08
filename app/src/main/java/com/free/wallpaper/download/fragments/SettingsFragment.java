package com.free.wallpaper.download.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import com.free.wallpaper.download.R;
import com.free.wallpaper.download.helpers.WallpaperScheduler;

/**
 * Created by Victor on 3/2/2017.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.app_preferences);
       listen();
    }

    public void listen(){
        findPreference("daily_wallpaper").setOnPreferenceChangeListener(new android.support.v7.preference.Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(android.support.v7.preference.Preference preference, Object newValue) {
                boolean state =  (boolean) newValue;
                WallpaperScheduler alarm = new WallpaperScheduler();

                if(state){
                    Log.e("Settings", "True");
                    alarm.setAlarm(getActivity());
                }
                else if(!state){
                    Log.e("Settings", "False");
                    alarm.cancelAlarm(getActivity());
                }
                return true;
            }
        });
    }
}
