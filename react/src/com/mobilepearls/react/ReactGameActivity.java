package com.mobilepearls.react;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;

public class ReactGameActivity extends Activity {

	private ReactView view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ReactSoundManager.initSounds(this);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		view = new ReactView(this, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		view.setFocusable(true);
		view.setFocusableInTouchMode(true);
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.restart_menuitem:
			view.restart();
			return true;
		case R.id.highscore_menuitem:
			startActivity(new Intent(this, ReactHighScoresActivity.class));
			return true;
		case R.id.about_menuitem:
			startActivity(new Intent(this, ReactAboutActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}