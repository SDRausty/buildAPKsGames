package pt.isec.tp.am;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}

	public void onBack(View v)
	{
		finish();
	}
	
}
