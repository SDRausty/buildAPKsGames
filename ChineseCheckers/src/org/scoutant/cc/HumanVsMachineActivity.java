package org.scoutant.cc;

import org.scoutant.cc.model.Game;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class HumanVsMachineActivity extends BaseActivity {
	public static final String KEY_NB_PLAYERS = "nb_players";
	
	private static final int[] ids = { R.id.player_0, R.id.player_1, R.id.player_2, R.id.player_3, R.id.player_4, R.id.player_5 };
	private CheckBox[] cbs = new CheckBox[6]; 
	
	private static String tag = "activity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.human_vs_machine);
		for (int i=0; i<ids.length; i++) populate( i);
		findViewById(R.id.play).setOnClickListener( new StartListener());
		if (gameOn()) {
			startActivity( new Intent(getApplicationContext(), UI.class));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if ( gameOn()) finish();
		if ( prefs.getInt(NbPlayersActivity.KEY_NB_PLAYERS, 99) < 0 ) finish(); 
	}

	private void populate(int player) {
		CheckBox view = (CheckBox) findViewById( ids[player]);
		if (view == null) {
			Log.e(tag, "No View ! ");
			return;
		}
		cbs[player] = view;
		view.setVisibility( playing(player) ? View.VISIBLE : View.INVISIBLE);
		view.setChecked(  player!=0 ? true: false);
		if (!Game.DEV) view.setOnCheckedChangeListener( new CheckChanged());
	}
	
	private class StartListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			save();
			startActivity( new Intent(getApplicationContext(), UI.class));
		}
	}
	
	private void save() {
        Editor editor = prefs.edit();
        for (int i=0; i<6; i++) {
        	editor.putBoolean(keys[i], cbs[i].isChecked());
        }
        editor.commit();
	}
	
	private class CheckChanged implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			boolean onlyAIs = cbs[0].isChecked()&&cbs[1].isChecked()&&cbs[2].isChecked()&&cbs[3].isChecked()&&cbs[4].isChecked()&&cbs[5].isChecked(); 
			setState( !onlyAIs);
		}
	}
	
	protected void setState( boolean state) {
		View view = findViewById(R.id.play); 
		view.setEnabled( state);
		view.setAlpha( state ? 0.8f : 0.3f );
	}
}