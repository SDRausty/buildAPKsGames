package org.scoutant.cc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MenuActivity extends Activity {
	
	public static final int RESULT_HELP = 9;
	public static final int RESULT_LOVE = 69;
	public static final int RESULT_BACK = -1;
	public static final int RESULT_RESUME = 0;
	public static final int RESULT_QUIT = -100;
	public static final int RESULT_NEW_GAME = 100;

	private static String tag = "activity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		bind(R.id.help, RESULT_HELP);
		bind(R.id.love, RESULT_LOVE);
		bind(R.id.resume, RESULT_RESUME);
		bind(R.id.back, RESULT_BACK);
		bind(R.id.newgame, RESULT_NEW_GAME);
		bind(R.id.quit, RESULT_QUIT);
	}
	
	private void bind(int viewId, int resultCode) {
		View view = findViewById(viewId);
		if (view == null) {
			Log.e(tag, "No View ! ");
			return;
		}
		view.setOnClickListener( new SetResultListener( resultCode)); 
		
	}
	
	private class SetResultListener implements OnClickListener {
		private int result;

		private SetResultListener(int result) {
			this.result = result;
		}

		@Override
		public void onClick(View v) {
			setResult(result);
			finish();
		}
	}
	
	
	
}
