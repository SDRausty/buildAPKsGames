package eu.veldsoft.russian.triple;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Splash screen.
 * 
 * @author Todor Balabanov
 */
public class SplashActivity extends Activity {
	/**
	 * Timeout for splash screen visualization.
	 */
	private long timeout = 0L;

	/**
	 * Name of the activity to be open after the timeout.
	 */
	private String redirect = "";

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		/*
		 * Activate JavaScript.
		 */
		((WebView) findViewById(R.id.ads)).getSettings().setJavaScriptEnabled(
				true);

		/*
		 * Load local web page as banner holder.
		 */
		((WebView) findViewById(R.id.ads))
				.loadUrl("file:///android_asset/banner.html");

		/*
		 * Get splash screen timeout.
		 */
		try {
			timeout = getPackageManager().getActivityInfo(
					this.getComponentName(),
					PackageManager.GET_ACTIVITIES
							| PackageManager.GET_META_DATA).metaData.getInt(
					"timeout", 0);
		} catch (Exception e) {
			timeout = 0;
		}

		/*
		 * Get redirect activity class name.
		 */
		try {
			redirect = getPackageManager().getActivityInfo(
					this.getComponentName(),
					PackageManager.GET_ACTIVITIES
							| PackageManager.GET_META_DATA).metaData
					.getString("redirect");
		} catch (Exception e) {
			redirect = this.getClass().toString();
			Toast.makeText(
					this,
					getResources().getString(
							R.string.redirect_activity_is_missing_message),
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onResume() {
		super.onResume();

		new Timer().schedule(new TimerTask() {
			public void run() {
				try {
					startActivity(new Intent(SplashActivity.this, Class
							.forName(redirect)));
				} catch (Exception e) {
				}
			}
		}, timeout);
	}
}
