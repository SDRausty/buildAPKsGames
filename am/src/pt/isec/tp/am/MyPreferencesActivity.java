package pt.isec.tp.am;

import android.os.Bundle;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.app.Activity;

public class MyPreferencesActivity extends PreferenceActivity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        //Preference customPref = (Preference) findPreference("Color");
       
    }

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	
}
