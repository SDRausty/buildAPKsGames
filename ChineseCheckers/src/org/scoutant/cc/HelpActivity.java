package org.scoutant.cc;

import org.scoutant.CommandListener;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;

public class HelpActivity extends DemoActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		findViewById(R.id.back).setOnClickListener( new CommandListener( new Finish()));
		game = new DemoGameView(this, 350);
		((ViewGroup) findViewById(R.id.container)).addView(game);
		new PlayMove().execute();
	}
	@Override
	public boolean onCreateOptionsMenu ( final android.view.Menu menu ) {
		getMenuInflater().inflate(R.menu.menu_back, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected ( final MenuItem item ) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()) {
			case R.id.menu_item_back:
				stopPlaying();
				finish();
				break;
			default: return super.onOptionsItemSelected(item);
		}
		return true;
	}

}
