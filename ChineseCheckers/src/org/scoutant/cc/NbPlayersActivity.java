package org.scoutant.cc;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class NbPlayersActivity extends BaseActivity {
	public static final String KEY_NB_PLAYERS = "nb_players";
	
	private static String tag = "activity";

	private boolean creating = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nb_players);
		bind(R.id.players_2, 2, new boolean[] { true, false, false, true , false, false});
		bind(R.id.players_3, 3, new boolean[] { true, false, true , false, true , false});
		bind(R.id.players_6, 6, new boolean[] { true, true , true , true , true , true });
		creating=true;
		if ( gameOn() ) {
			startActivity( new Intent(getApplicationContext(), UI.class));
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (creating) {
			creating=false;
			return;
		}
		if ( gameOn()) finish();
	}
	
	private void bind(int viewId, int nbPlayers, boolean[] playings) {
		View view = findViewById(viewId);
		if (view == null) {
			Log.e(tag, "No View ! ");
			return;
		}
		view.setOnClickListener( new NbPlayersListener( nbPlayers)); 
		
	}
	
	private class NbPlayersListener implements OnClickListener {
		private int nb;
		private NbPlayersListener(int nb) {
			this.nb = nb;
		}

		@Override
		public void onClick(View v) {
			save(nb);
			startActivity( new Intent(getApplicationContext(), HumanVsMachineActivity.class));
		}
	}

	private void save(int nb) {
        Editor editor = prefs.edit();
        editor.putInt(KEY_NB_PLAYERS, nb);
        editor.commit();
	}
	
}
