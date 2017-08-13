package com.mobilepearls.react;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class ReactAboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);

		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			TextView aboutText = (TextView) findViewById(R.id.about_text);
			aboutText.setText(aboutText.getText().toString().replace("$VERSION$", packageInfo.versionName));
		} catch (NameNotFoundException e) {
			Log.e(getClass().getSimpleName(), e.getMessage(), e);
		}

	}

}
