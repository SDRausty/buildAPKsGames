package org.scoutant.cc;

import org.scoutant.cc.model.Game;
import org.scoutant.cc.model.Log;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppRater {
    private final static int DAYS_UNTIL_PROMPT = 2;
    private final static int LAUNCHES_UNTIL_PROMPT = 6;

    public static boolean shallAskForRate(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( context);
        if (prefs.getBoolean("dontshowagain", false)) { return false; }
        
        SharedPreferences.Editor editor = prefs.edit();
        
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        editor.commit();
        
        if (Game.LOG) Log.d("activity", "launch_count : " + launch_count + ", nb days : " + (System.currentTimeMillis()-date_firstLaunch)/24/60/60/1000);
        
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
            	return true;
            }
        }
        return false;
    }   
}
