package org.scoutant.cc;

import org.scoutant.Command;
import org.scoutant.CommandListener;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;

public class RateActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rate);
		findViewById(R.id.yes).setOnClickListener( new CommandListener( new Reset()));
		bind( R.id.yes, new Rate());
		bind( R.id.later, new Reset());
		bind( R.id.no, new DontShowAgain());
	}

	
	private class Rate implements Command {
		@Override
		public void execute() {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=org.scoutant.cc")); 
			startActivity(intent);
			finish();
		}
	}
	
	private class Reset implements Command {
		@Override
		public void execute() {
			resetCounter();
			finish();
		}
	}
	private class DontShowAgain implements Command {
		@Override
		public void execute() {
			Editor editor = prefs.edit();
            if (editor != null) editor.putBoolean("dontshowagain", true).commit();
			finish();
		}
	}
	
	private void resetCounter() {
		Editor editor = prefs.edit();
		if (editor== null) return;
		editor.putLong("launch_count", 0);
		editor.putLong("date_firstlaunch", 0);
		editor.commit();
	}

}
